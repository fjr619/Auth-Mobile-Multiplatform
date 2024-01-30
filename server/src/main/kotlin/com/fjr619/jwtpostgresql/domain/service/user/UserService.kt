package com.fjr619.jwtpostgresql.domain.service.user

import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.UserError
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.github.michaelbull.result.Result

interface UserService {
    suspend fun findByEmail(email: String): Result<User, UserError>
    suspend fun findById(id: Long): Result<User, UserError>
    suspend fun save(dto: UserCreateDto): Result<User, UserError>
    suspend fun checkUserNameAndPassword(dto: UserLoginDto): Result<User, UserError>
    suspend fun isAdmin(id: Long): Result<Boolean, UserError>
}