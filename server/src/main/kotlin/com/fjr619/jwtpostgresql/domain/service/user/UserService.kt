package com.fjr619.jwtpostgresql.domain.service.user

import com.fjr619.jwtpostgresql.domain.model.RequestError
import com.fjr619.jwtpostgresql.domain.model.entity.User
import dto.UserCreateDto
import dto.UserLoginDto
import com.github.michaelbull.result.Result

interface UserService {
    suspend fun findByEmail(email: String): Result<User, RequestError>
    suspend fun findById(id: Long): Result<User, RequestError>
    suspend fun save(dto: UserCreateDto): Result<User, RequestError>
    suspend fun checkUserNameAndPassword(dto: UserLoginDto): Result<User, RequestError>
    suspend fun isAdmin(id: Long): Result<Boolean, RequestError>
}