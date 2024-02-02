package com.fjr619.jwtpostgresql.presentation.error

import data.Response
import com.fjr619.jwtpostgresql.domain.model.RequestError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.handleRequestError(
    error: Any
) {
    when (error) {
        is RequestError.BadRequest -> call.respond(
            HttpStatusCode.BadRequest, Response.ErrorResponse(
                statusCode = HttpStatusCode.BadRequest.value,
                message = error.message
            ).toResponse()
        )

        is RequestError.NotFound -> call.respond(
            HttpStatusCode.NotFound,
            Response.ErrorResponse(
                statusCode = HttpStatusCode.NotFound.value,
                message = error.message).toResponse()
        )

        is RequestError.Unauthorized -> call.respond(
            HttpStatusCode.Unauthorized,
            Response.ErrorResponse(
                statusCode = HttpStatusCode.Unauthorized.value,
                message = error.message).toResponse()
        )

        is RequestError.Forbidden -> call.respond(
            HttpStatusCode.Forbidden,
            Response.ErrorResponse(
                statusCode = HttpStatusCode.Forbidden.value,
                message = error.message).toResponse()
        )

        is RequestError.BadCredentials -> call.respond(
            HttpStatusCode.BadRequest,
            Response.ErrorResponse(
                statusCode = HttpStatusCode.BadRequest.value,
                message = error.message).toResponse()
        )

        is RequestError.BadRole -> call.respond(
            HttpStatusCode.Forbidden,
            Response.ErrorResponse(
                statusCode = HttpStatusCode.Forbidden.value,
                message = error.message).toResponse()
        )
    }
}