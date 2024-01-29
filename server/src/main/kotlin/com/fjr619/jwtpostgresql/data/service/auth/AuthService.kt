package com.fjr619.jwtpostgresql.data.service.auth

import models.User
import com.fjr619.jwtpostgresql.presentation.routes.auth.CreateUserParams
import kotlin.jvm.Throws

interface AuthService {
    suspend fun registerUser(params: CreateUserParams): User
    @Throws(Exception::class)
    suspend fun loginUser(email: String, password: String): User

    @Throws(Exception::class)
    suspend fun findUserByEmail(email: String): User?

}