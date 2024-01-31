package com.fjr619.jwtpostgresql.presentation.routes

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.base.ServiceResultSerializer
import com.fjr619.jwtpostgresql.domain.model.entity.User
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenClaim
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.fjr619.jwtpostgresql.domain.service.user.UserService
import com.fjr619.jwtpostgresql.presentation.error.getUserId
import com.fjr619.jwtpostgresql.presentation.error.handleRequestError
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

private const val ENDPOINT = "api/users" // Endpoint

fun Application.userRoutes() {

    val userService: UserService by inject()
    val tokenConfig: TokenConfig by inject()
    val tokenService: TokenService by inject()

    routing {
        route(ENDPOINT) {

            post("register", {
                tags = listOf("USER")
                description = "Register User"
                request {
                    body<UserCreateDto> {}
                }
                response {
                    HttpStatusCode.Created to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<UserDto>> {
                            example(
                                "SUCCESS", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.SUCCESS,
                                    data = UserDto(
                                        id = 14,
                                        fullName = "Full Name",
                                        avatar = "Avatar",
                                        email = "aaa6@aaa.com",
                                        createdAt = "2024-01-31T04:22:20.513363",
                                        updatedAt = "2024-01-31T04:22:20.513363"
                                    ),
                                    statusCode = 201
                                )
                            )
                        }
                    }

                    HttpStatusCode.BadRequest to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<Nothing>> {
                            example(
                                "Error", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.ERROR,
                                    data = null,
                                    message = "error message",
                                    statusCode = 400
                                )
                            )
                        }

                    }
                }
            })
            {
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

            post("login", {
                tags = listOf("USER")
                description = "Login User"
                request {
                    body<UserLoginDto> {
                        example(
                            "LOGIN", UserLoginDto(
                                email = "aaa2@aaa.com",
                                password = "1234"
                            )
                        )
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<UserDto>> {
                            example(
                                "SUCCESS", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.SUCCESS,
                                    data = UserDto(
                                        id = 14,
                                        fullName = "Full Name",
                                        avatar = "Avatar",
                                        email = "aaa6@aaa.com",
                                        createdAt = "2024-01-31T04:22:20.513363",
                                        updatedAt = "2024-01-31T04:22:20.513363"
                                    ),
                                    statusCode = 200
                                )
                            )
                        }
                    }

                    HttpStatusCode.BadRequest to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<Nothing>> {
                            example(
                                "Error", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.ERROR,
                                    data = null,
                                    message = "error message",
                                    statusCode = 400
                                )
                            )
                        }

                    }
                }
            })
            {
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

            authenticate {
                get("me", {
                    tags = listOf("USER")
                    description = "Get Info Current User"
                    securitySchemeNames = setOf("JWT-Auth")
                }) {
                    call.respond(
                        HttpStatusCode.OK,
                        BaseResponse.SuccessResponse("api user") as BaseResponse<String>
                    )
                }

                //test admin scoope
                get("me/admin", {
                    tags = listOf("USER")
                    securitySchemeNames = setOf("JWT-Auth")
                }) {
                    val userId = getUserId()
                    userService.isAdmin(userId).mapBoth(
                        success = {
                            call.respond(
                                HttpStatusCode.OK,
                                BaseResponse.SuccessResponse(data = "User Admin").toResponse()
                            )
                        },
                        failure = {
                            handleRequestError(it)
                        }
                    )
                }
            }
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

fun generateToken(tokenConfig: TokenConfig, tokenService: TokenService, userId: Long, email: String): String {
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
