package com.fjr619.jwtpostgresql.domain.service.auth

import com.fjr619.jwtpostgresql.domain.model.GENERIC_ERROR
import com.fjr619.jwtpostgresql.domain.model.USER_ALREADY_REGISTERED
import com.fjr619.jwtpostgresql.domain.model.USER_LOGIN_FAILURE
import com.fjr619.jwtpostgresql.domain.model.USER_NOT_FOUND
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.UserError
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toModel
import com.fjr619.jwtpostgresql.domain.repository.auth.AuthRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import mu.KLogger
import org.koin.core.annotation.Singleton

@Singleton
class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val logger:KLogger
) : AuthService {
    override suspend fun findByEmail(email: String): Result<User, UserError> {
        return authRepository.findByEmail(email)?.let {
            Ok(it)
        } ?: Err(UserError.NotFound(USER_NOT_FOUND))
    }

    override suspend fun save(dto: UserCreateDto): Result<User, UserError> {
        return findByEmail(dto.email).onSuccess {
            return Err(UserError.BadRequest(USER_ALREADY_REGISTERED))
        }.onFailure {
            return authRepository.save(dto.toModel())?.let {
                Ok(it)
            } ?: Err(UserError.BadRequest(GENERIC_ERROR))
        }
    }

    override suspend fun checkUserNameAndPassword(dto: UserLoginDto): Result<User, UserError> {
        return authRepository.checkUserNameAndPassword(dto.email, dto.password)?.let {
            Ok(it)
        } ?: Err(UserError.BadCredentials(USER_LOGIN_FAILURE))
    }
}