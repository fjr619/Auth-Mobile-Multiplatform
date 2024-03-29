package com.fjr619.jwtpostgresql.presentation.swagger

import com.fjr619.jwtpostgresql.base.ServiceResultSerializer
import com.fjr619.jwtpostgresql.domain.model.dto.UserCreateDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserDto
import com.fjr619.jwtpostgresql.domain.model.dto.UserLoginDto
import io.github.smiley4.ktorswaggerui.dsl.OpenApiResponses
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode

private const val TAG = "USER"

fun OpenApiResponses.failure() {
    HttpStatusCode.BadRequest to {
        body<ServiceResultSerializer.ServiceResultSurrogate<Nothing>> {
            example(
                "Error", ServiceResultSerializer.ServiceResultSurrogate(
                    type = ServiceResultSerializer.ServiceResultSurrogate.Type.ERROR,
                    data = null,
                    message = "error message",
                    statusCode = 400,
                    token = null
                )
            )
        }
    }
}

fun OpenApiRoute.swaggerRegister() {
    tags = listOf(TAG)
    description = "Register User"

    request {
        body<UserCreateDto> {
            example("REGISTER", UserCreateDto(
                fullName = "Ini fullname user",
                email = "iniemail@user.com",
                avatar = "ini avatar user",
                password = "ini password user"
            ))
        }
    }

    response {
        HttpStatusCode.Created to {
            body<ServiceResultSerializer.ServiceResultSurrogate<UserDto>>()
        }

        failure()
    }
}

fun OpenApiRoute.swaggerLogin() {
    tags = listOf(TAG)
    description = "Login User"
    request {
        body<UserLoginDto> {
            example(
                "LOGIN", UserLoginDto(
                    email = "aaa6@aaa.com",
                    password = "1234"
                )
            )
        }
    }
    response {
        HttpStatusCode.OK to {
            body<ServiceResultSerializer.ServiceResultSurrogate<UserDto>>()
        }

        failure()
    }
}