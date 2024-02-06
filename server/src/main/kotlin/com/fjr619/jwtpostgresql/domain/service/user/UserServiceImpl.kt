package com.fjr619.jwtpostgresql.domain.service.user

import GENERIC_ERROR
import com.fjr619.jwtpostgresql.domain.model.RequestError
import USER_ALREADY_REGISTERED
import USER_LOGIN_FAILURE
import USER_NOT_ADMIN
import NOT_FOUND
import com.fjr619.jwtpostgresql.domain.model.entity.User
import dto.UserCreateDto
import dto.UserLoginDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toUser
import com.fjr619.jwtpostgresql.domain.repository.UserRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import mu.KLogger
import org.koin.core.annotation.Singleton

@Singleton
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val logger: KLogger
) : UserService {
    override suspend fun findByEmail(email: String): Result<User, RequestError> {
        return userRepository.findByEmail(email)?.let {
            Ok(it)
        } ?: Err(RequestError.NotFound(NOT_FOUND))
    }

    override suspend fun findById(id: Long): Result<User, RequestError> {
        return userRepository.findById(id)?.let {
            Ok(it)
        } ?: Err(RequestError.NotFound(NOT_FOUND))
    }

    override suspend fun save(dto: UserCreateDto): Result<User, RequestError> {
        return findByEmail(dto.email).onSuccess {
            return Err(RequestError.BadRequest(USER_ALREADY_REGISTERED))
        }.onFailure {
            return userRepository.save(dto.toUser())?.let {
                Ok(it)
            } ?: Err(RequestError.BadRequest(GENERIC_ERROR))
        }
    }

    override suspend fun checkUserNameAndPassword(dto: UserLoginDto): Result<User, RequestError> {
        return userRepository.checkUserNameAndPassword(dto.email, dto.password)?.let {
            Ok(it)
        } ?: Err(RequestError.BadCredentials(USER_LOGIN_FAILURE))
    }

    override suspend fun isAdmin(id: Long): Result<Boolean, RequestError> {
        return findById(id).andThen {
            if (it.role == User.Role.ADMIN) {
                Ok(true)
            } else {
                Err(RequestError.BadRole(USER_NOT_ADMIN))
            }
        }
    }
}