package com.fjr619.jwtpostgresql.domain.repository.auth

import com.fjr619.jwtpostgresql.data.db.UserEntity
import com.fjr619.jwtpostgresql.domain.model.params.CreateUserParams
import kotlin.jvm.Throws

interface AuthRepository {
    suspend fun registerUser(params: CreateUserParams): UserEntity
    @Throws(Exception::class)
    suspend fun loginUser(email: String, password: String): UserEntity

    @Throws(Exception::class)
    suspend fun findUserByEmail(email: String): UserEntity?

}