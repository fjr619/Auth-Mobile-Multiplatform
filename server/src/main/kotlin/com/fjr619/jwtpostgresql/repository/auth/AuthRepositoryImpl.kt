package com.fjr619.jwtpostgresql.repository.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import com.fjr619.jwtpostgresql.routes.auth.UserLoginParams
import com.fjr619.jwtpostgresql.service.auth.AuthService

class AuthRepositoryImpl(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun registeruser(params: CreateUserParams): BaseResponse<Any> {
        return try {
            if (isEmailExist(params.email)) {
                BaseResponse.ErrorResponse(message = "Email already registered")
            } else {
                val user = authService.registerUser(params)
                if (user != null) {
                    //@TODO generate authentication token for the user
                    BaseResponse.SuccessResponse(data = user)
                } else {
                    BaseResponse.ErrorResponse()
                }
            }
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message)
        }

    }

    override suspend fun loginUser(params: UserLoginParams): BaseResponse<Any> {
        val user = authService.loginUser(params.email, params.password)
        return BaseResponse.SuccessResponse(data = user)
    }

    private suspend fun isEmailExist(email: String): Boolean {
        return authService.findUserByEmail(email = email) != null
    }
}