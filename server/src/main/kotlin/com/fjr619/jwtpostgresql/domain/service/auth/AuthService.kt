package com.fjr619.jwtpostgresql.domain.service.auth

import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.UserError
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.github.michaelbull.result.Result

interface AuthService {
    suspend fun findByEmail(email: String): Result<User, UserError>
    suspend fun save(dto: UserCreateDto): Result<User, UserError>
    suspend fun checkUserNameAndPassword(dto: UserLoginDto): Result<User, UserError>
}