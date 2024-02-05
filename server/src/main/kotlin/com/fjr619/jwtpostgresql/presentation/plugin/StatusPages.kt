package com.fjr619.jwtpostgresql.presentation.plugin

import Response
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

//class ValidationException(override val message: String) : Throwable()
//class ParsingException(override val message: String) : Throwable()
//class GenericException(override val message: String = "Some error occurred! Please try again late"): Throwable()

/**
 * Configure the Status Pages plugin and configure it
 * https://ktor.io/docs/status-pages.html
 * We use status pages to respond with expected exceptions
 */
fun Application.configureExceptions() {
    install(StatusPages) {
//        exception<Exception> { call, throwable ->
//            when (throwable) {
//                 is RequestValidationException -> {
//                    call.respond(
//                        HttpStatusCode.BadRequest,
//                        BaseResponse.ErrorResponse(
//                            statusCode = HttpStatusCode.BadRequest,
//                            message = throwable.message) as BaseResponse<Nothing>
//                    )
//                }
//            }
//        }

        exception<Exception> { call, exception ->
            when(exception) {
                is RequestValidationException -> {
                    call.respond(HttpStatusCode.BadRequest, Response.ErrorResponse(
                        statusCode = HttpStatusCode.BadRequest.value,
                        message = exception.reasons.joinToString()
                    ) as Response<Nothing>
                    )
                }
            }
        }



        status(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
            HttpStatusCode.Unauthorized
        ) { call, statusCode ->
            val message: Response<Nothing> = Response.ErrorResponse(
                statusCode = statusCode.value,
                message = when (statusCode) {
                    HttpStatusCode.InternalServerError -> "Oops! internal server error at our end"
                    HttpStatusCode.BadGateway -> "Oops! We got a bad gateway. Fixing it. Hold on!"
                    HttpStatusCode.Unauthorized -> "Oops! You must login first!"
                    else -> ""
                }
            )
            call.respond(
                statusCode,
                message
            )
        }
    }
}