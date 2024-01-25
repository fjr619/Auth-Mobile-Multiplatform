package com.fjr619.jwtpostgresql.service.auth

import com.fjr619.jwtpostgresql.models.User
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams

interface AuthService {
    suspend fun registerUser(params: CreateUserParams): User?
    suspend fun findUserByEmail(email: String): User?

}