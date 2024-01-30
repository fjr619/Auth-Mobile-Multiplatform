package com.fjr619.jwtpostgresql.presentation.routes.auth

import com.fjr619.jwtpostgresql.base.ServiceResultSerializer
import com.fjr619.jwtpostgresql.domain.model.User
import com.fjr619.jwtpostgresql.domain.model.params.UserLoginParams
import com.fjr619.jwtpostgresql.domain.model.params.UserRegisterParams
import com.fjr619.jwtpostgresql.domain.service.auth.AuthService
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.authRoutes() {

    val authService: AuthService by inject()

    routing {
        route("/auth") {

            post("/register", {
                tags = listOf("AUTH")
                description = "Register User"
                request {
                    body<UserRegisterParams> {}
                }
                response {
                    HttpStatusCode.OK to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<User>> {
                            example(
                                "SUCCESS", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.SUCCESS,
                                    data = User(
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
                val params = call.receive<UserRegisterParams>()
                val result = authService.registeruser(params)
                call.respond(result.statusCode, result)
            }

            post("/login", {
                tags = listOf("AUTH")
                description = "Login User"
                request {
                    body<UserLoginParams> {
                        example("LOGIN", UserLoginParams(
                            email = "aaa6@aaa.com",
                            password = "1234"
                        ))
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        body<ServiceResultSerializer.ServiceResultSurrogate<User>> {
                            example(
                                "SUCCESS", ServiceResultSerializer.ServiceResultSurrogate(
                                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.SUCCESS,
                                    data = User(
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
                val params = call.receive<UserLoginParams>()
                val result = authService.loginUser(params)
                call.respond(result.statusCode, result)
            }
        }
    }
}