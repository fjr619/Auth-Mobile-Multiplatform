package com.fjr619.jwtpostgresql.repository.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.routes.auth.CreateUserParams
import com.fjr619.jwtpostgresql.routes.auth.UserLoginParams
import com.fjr619.jwtpostgresql.security.token.TokenClaim
import com.fjr619.jwtpostgresql.security.token.TokenConfig
import com.fjr619.jwtpostgresql.security.token.TokenService
import com.fjr619.jwtpostgresql.service.auth.AuthService
import models.User

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig
) : AuthRepository {
    override suspend fun registeruser(params: CreateUserParams): BaseResponse<User> {
        val user = authService.registerUser(params)
        user.authToken = user.generateToken()
        return BaseResponse.SuccessResponse(data = user)
    }

    override suspend fun loginUser(params: UserLoginParams): BaseResponse<User> {
        val user = authService.loginUser(params.email, params.password)
        user.authToken = user.generateToken()
        return BaseResponse.SuccessResponse(data = user)
    }

    private fun User.generateToken(): String {
        return tokenService.generate(
            config = tokenConfig,
            claims = arrayOf(
                TokenClaim(
                    name = "userId",
                    value = this.id.toString()
                ),
                TokenClaim(
                    name = "email",
                    value = this.email
                )
            )
        )
    }

//    private suspend fun isEmailExist(email: String): Boolean {
//        return authService.findUserByEmail(email = email) != null
//    }
}