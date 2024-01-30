package com.fjr619.jwtpostgresql.domain.repository.auth

import com.fjr619.jwtpostgresql.data.db.UserEntity
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.params.UserRegisterParams
import kotlin.jvm.Throws

interface AuthRepository {
    suspend fun registerUser(params: UserRegisterParams): User
    @Throws(Exception::class)
    suspend fun loginUser(email: String, password: String): User
}