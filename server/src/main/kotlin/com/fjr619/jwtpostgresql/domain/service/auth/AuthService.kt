package com.fjr619.jwtpostgresql.domain.service.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.params.CreateUserParams
import com.fjr619.jwtpostgresql.domain.model.params.UserLoginParams

interface AuthService {
    suspend fun registeruser(params: CreateUserParams): BaseResponse<User>
    suspend fun loginUser(params: UserLoginParams): BaseResponse<User>
}