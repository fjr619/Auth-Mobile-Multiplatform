package com.fjr619.jwtpostgresql.repository.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import com.fjr619.jwtpostgresql.routes.auth.UserLoginParams
import models.User

interface AuthRepository {
    suspend fun registeruser(params: CreateUserParams): BaseResponse<User>
    suspend fun loginUser(params: UserLoginParams): BaseResponse<User>
}