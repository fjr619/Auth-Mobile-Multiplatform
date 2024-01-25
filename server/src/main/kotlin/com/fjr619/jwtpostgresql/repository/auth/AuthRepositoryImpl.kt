package com.fjr619.jwtpostgresql.repository.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import com.fjr619.jwtpostgresql.routes.auth.UserLoginParams
import com.fjr619.jwtpostgresql.service.auth.AuthService
import models.User

class AuthRepositoryImpl(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun registeruser(params: CreateUserParams): BaseResponse<User> {
        val user = authService.registerUser(params)
        return BaseResponse.SuccessResponse(data = user)
    }

    override suspend fun loginUser(params: UserLoginParams): BaseResponse<User> {
        val user = authService.loginUser(params.email, params.password)
        return BaseResponse.SuccessResponse(data = user)
    }

//    private suspend fun isEmailExist(email: String): Boolean {
//        return authService.findUserByEmail(email = email) != null
//    }
}