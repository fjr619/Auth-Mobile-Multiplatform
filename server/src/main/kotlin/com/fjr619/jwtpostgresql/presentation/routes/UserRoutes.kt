package com.fjr619.jwtpostgresql.presentation.routes

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.fjr619.jwtpostgresql.domain.model.entity.User
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenClaim
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.fjr619.jwtpostgresql.domain.service.user.UserService
import com.fjr619.jwtpostgresql.presentation.error.handleRequestError
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerLogin
import com.fjr619.jwtpostgresql.presentation.swagger.swaggerRegister
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

private const val ENDPOINT = "api/user" // Endpoint

fun Application.userRoutes() {

    val userService: UserService by inject()
    val tokenConfig: TokenConfig by inject()
    val tokenService: TokenService by inject()

    routing {
        route(ENDPOINT) {

            post("register", { swaggerRegister() }) {
                val dto = call.receive<UserCreateDto>()
                userService.save(dto)
                    .mapBoth(
                        success = {
                            val token = it.generateToken(tokenConfig, tokenService)
                            call.respond(
                                HttpStatusCode.Created, BaseResponse.SuccessResponse(
                                    statusCode = HttpStatusCode.Created,
                                    data = it.toDto(),
                                    authToken = token
                                ).toResponse()
                            )
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
            }

            post("login", { swaggerLogin() }) {
                val dto = call.receive<UserLoginDto>()
                userService.checkUserNameAndPassword(dto).mapBoth(
                    success = {
                        val token = it.generateToken(tokenConfig, tokenService)
                        call.respond(
                            HttpStatusCode.OK, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.OK,
                                data = it.toDto(),
                                authToken = token
                            ).toResponse()
                        )
                    },
                    failure = {
                        handleRequestError(it)
                    }
                )
            }

//            authenticate {
//                //test admin scoope
//                get("me/admin", {
//                    tags = listOf("USER")
//                    securitySchemeNames = setOf("JWT-Auth")
//                }) {
//                    val userId = getUserId()
//                    userService.isAdmin(userId).mapBoth(
//                        success = {
//                            call.respond(
//                                HttpStatusCode.OK,
//                                BaseResponse.SuccessResponse(data = "User Admin").toResponse()
//                            )
//                        },
//                        failure = {
//                            handleRequestError(it)
//                        }
//                    )
//                }
//            }
        }

    }
}

fun User.generateToken(tokenConfig: TokenConfig, tokenService: TokenService): String {
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

fun generateToken(
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    userId: Long,
    email: String
): String {
    return tokenService.generate(
        config = tokenConfig,
        claims = arrayOf(
            TokenClaim(
                name = "userId",
                value = userId.toString()
            ),
            TokenClaim(
                name = "email",
                value = email
            )
        )
    )
}
