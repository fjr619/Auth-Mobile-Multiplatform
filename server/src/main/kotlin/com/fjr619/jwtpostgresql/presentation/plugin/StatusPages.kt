package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.base.BaseResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

class ValidationException(override val message: String) : Throwable()
class ParsingException(override val message: String) : Throwable()
class GenericException(override val message: String = "Some error occurred! Please try again late"): Throwable()

/**
 * Configure the Status Pages plugin and configure it
 * https://ktor.io/docs/status-pages.html
 * We use status pages to respond with expected exceptions
 */
fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is ValidationException, is RequestValidationException, is GenericException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse.ErrorResponse(
                            statusCode = HttpStatusCode.BadRequest,
                            message = throwable.message) as BaseResponse<Nothing>
                    )
                }

                is ParsingException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BaseResponse.ErrorResponse(
                            statusCode = HttpStatusCode.NotFound,
                            message = throwable.message) as BaseResponse<Nothing>
                    )
                }
            }
        }

        // This is a custom exception we use to respond with a 400 if a validation fails, Bad Request
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = cause.reasons.joinToString()
            ) as BaseResponse<Nothing>)
        }

        status(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
            HttpStatusCode.Unauthorized
        ) { call, statusCode ->
            val message: BaseResponse<Nothing> = BaseResponse.ErrorResponse(
                statusCode = statusCode,
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