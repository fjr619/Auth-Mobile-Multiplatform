package com.fjr619.jwtpostgresql.service.auth

import com.fjr619.jwtpostgresql.models.User
import com.fjr619.jwtpostgresql.plugin.ParsingException
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import kotlin.jvm.Throws

interface AuthService {
    suspend fun registerUser(params: CreateUserParams): User?
    @Throws(Exception::class)
    suspend fun loginUser(email: String, password: String): User?

    @Throws(Exception::class)
    suspend fun findUserByEmail(email: String): User?

}