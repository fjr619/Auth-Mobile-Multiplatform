package com.fjr619.jwtpostgresql.plugin

import com.fjr619.jwtpostgresql.base.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

class ValidationException(override val message: String): Throwable()
class ParsingException(override val message: String): Throwable()

fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when(throwable) {
                is ValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse.ErrorResponse<Any>(statusCode = HttpStatusCode.BadRequest, message = "Validation : ${throwable.message}")
                    )
                }
                is ParsingException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BaseResponse.ErrorResponse<Any>(statusCode = HttpStatusCode.NotFound, message = throwable.message)
                    )
                }
            }
        }

        status(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
            HttpStatusCode.Unauthorized
        ) { call, statusCode ->
            call.respond(
                statusCode,
                BaseResponse.ErrorResponse<Any>(
                    statusCode = statusCode,
                    message = when (statusCode) {
                        HttpStatusCode.InternalServerError -> "Oops! internal server error at our end"
                        HttpStatusCode.BadGateway -> "Oops! We got a bad gateway. Fixing it. Hold on!"
                        HttpStatusCode.Unauthorized -> "Oops! You must login first!"
                        else -> ""
                    }
                )
            )
//            when(statusCode) {
//                HttpStatusCode.InternalServerError -> {
//                    call.respond(
//                        statusCode,
//                        BaseResponse.ErrorResponse<Any>(statusCode = statusCode, message = "Oops! internal server error at our end")
//                    )
//                }
//                HttpStatusCode.BadGateway -> {
//                    call.respond(
//                        statusCode,
//                        BaseResponse.ErrorResponse<Any>(statusCode = statusCode, message = "Oops! We got a bad gateway. Fixing it. Hold on!")
//                    )
//                }
//                HttpStatusCode.Unauthorized -> {
//                    call.respond(
//                        statusCode,
//                        BaseResponse.ErrorResponse<Any>(statusCode = statusCode, message = "Oops! You must login first!")
//                    )
//                }
//            }
        }
    }
}