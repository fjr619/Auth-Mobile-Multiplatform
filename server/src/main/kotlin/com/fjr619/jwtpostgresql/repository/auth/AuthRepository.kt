package com.fjr619.jwtpostgresql.repository.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams

interface AuthRepository {
    suspend fun registeruser(params: CreateUserParams): BaseResponse<Any>
    suspend fun loginUser(email: String, password: String): BaseResponse<Any>
}