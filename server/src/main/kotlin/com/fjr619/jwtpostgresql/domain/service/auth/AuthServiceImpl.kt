package com.fjr619.jwtpostgresql.domain.service.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.params.UserRegisterParams
import com.fjr619.jwtpostgresql.domain.model.params.UserLoginParams
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenClaim
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.fjr619.jwtpostgresql.data.mapper.toModel
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.repository.auth.AuthRepository
import org.koin.core.annotation.Singleton

@Singleton
class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService,
    private val tokenConfig: TokenConfig
) : AuthService {
    override suspend fun registeruser(params: UserRegisterParams): BaseResponse<User> {
        val user = authRepository.registerUser(params).toModel()
        user.authToken = user.generateToken()
        return BaseResponse.SuccessResponse(data = user)
    }

    override suspend fun loginUser(params: UserLoginParams): BaseResponse<User> {
        val user = authRepository.loginUser(params.email, params.password).toModel()
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