package com.fjr619.jwtpostgresql.presentation.routes.auth

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.base.ServiceResultSerializer
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.UserError
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toDto
import com.fjr619.jwtpostgresql.domain.model.mapper.toModel
import com.fjr619.jwtpostgresql.domain.service.auth.AuthService
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenClaim
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import com.github.michaelbull.result.mapBoth
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import mu.KLogger
import org.koin.ktor.ext.inject

fun Application.authRoutes() {

    val authService: AuthService by inject()
    val tokenConfig: TokenConfig by inject()
    val tokenService: TokenService by inject()

    routing {
        route("/auth") {

            post("/register", {
                tags = listOf("AUTH")
                description = "Register User"
                request {
                    body<UserCreateDto> {}
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
                                        authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJteS1zdG9yeS1hcHAiLCJpc3MiOiJteS1zdG9yeS1hcHAiLCJleHAiOjE3MzgwNjEyNTMsInVzZXJJZCI6IjE0IiwiZW1haWwiOiJhYWE2QGFhYS5jb20ifQ.lVBGy7zH-QaQ1E2lSGYBHKcq5z5-2gNWQBhI0XZRcM4"
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
            }) {
                val dto = call.receive<UserCreateDto>()
                authService.save(dto)
                    .mapBoth(
                        success = {
                            val token = it.generateToken(tokenConfig, tokenService)
                            val dataResponse = it.toDto()
                            dataResponse.authToken = token
                            call.respond(HttpStatusCode.Created, BaseResponse.SuccessResponse(
                                statusCode = HttpStatusCode.Created,
                                data = dataResponse).toResponse())
                        },
                        failure = {
                            handleUserError(it)
                        }
                    )
            }

            post("/login", {
                tags = listOf("AUTH")
                description = "Login User"
                request {
                    body<UserLoginDto> {
                        example("LOGIN", UserLoginDto(
                            email = "aaa2@aaa.com",
                            password = "1234"
                        ))
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
                                        authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJteS1zdG9yeS1hcHAiLCJpc3MiOiJteS1zdG9yeS1hcHAiLCJleHAiOjE3MzgwNjEyNTMsInVzZXJJZCI6IjE0IiwiZW1haWwiOiJhYWE2QGFhYS5jb20ifQ.lVBGy7zH-QaQ1E2lSGYBHKcq5z5-2gNWQBhI0XZRcM4"
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
            }) {
                val dto = call.receive<UserLoginDto>()
                authService.checkUserNameAndPassword(dto).mapBoth(
                    success = {
                        val token = it.generateToken(tokenConfig, tokenService)
                        val dataResponse = it.toDto()
                        dataResponse.authToken = token
                        call.respond(HttpStatusCode.Created, BaseResponse.SuccessResponse(
                            statusCode = HttpStatusCode.Created,
                            data = dataResponse).toResponse())
                    },
                    failure = {
                        handleUserError(it)
                    }
                )
            }
        }
    }
}

    private fun User.generateToken(tokenConfig: TokenConfig, tokenService: TokenService): String {
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
private suspend fun PipelineContext<Unit, ApplicationCall>.handleUserError(
    error: Any
) {
    when (error) {
        // Users
        is UserError.BadRequest -> call.respond(HttpStatusCode.BadRequest, BaseResponse.ErrorResponse(message = error.message).toResponse())
        is UserError.NotFound -> call.respond(HttpStatusCode.NotFound, BaseResponse.ErrorResponse(message = error.message).toResponse())
        is UserError.Unauthorized -> call.respond(HttpStatusCode.Unauthorized, BaseResponse.ErrorResponse(message = error.message).toResponse())
        is UserError.Forbidden -> call.respond(HttpStatusCode.Forbidden, BaseResponse.ErrorResponse(message = error.message).toResponse())
        is UserError.BadCredentials -> call.respond(HttpStatusCode.BadRequest, BaseResponse.ErrorResponse(message = error.message).toResponse())
        is UserError.BadRole -> call.respond(HttpStatusCode.Forbidden, BaseResponse.ErrorResponse(message = error.message).toResponse())
    }
}