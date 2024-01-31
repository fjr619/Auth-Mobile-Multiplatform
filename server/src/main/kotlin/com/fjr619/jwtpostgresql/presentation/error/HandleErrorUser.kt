package com.fjr619.jwtpostgresql.presentation.error

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.RequestError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.handleRequestError(
    error: Any
) {
    when (error) {
        // Users
        is RequestError.BadRequest -> call.respond(
            HttpStatusCode.BadRequest, BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = error.message
            ).toResponse()
        )

        is RequestError.NotFound -> call.respond(
            HttpStatusCode.NotFound,
            BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.NotFound,
                message = error.message).toResponse()
        )

        is RequestError.Unauthorized -> call.respond(
            HttpStatusCode.Unauthorized,
            BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.Unauthorized,
                message = error.message).toResponse()
        )

        is RequestError.Forbidden -> call.respond(
            HttpStatusCode.Forbidden,
            BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.Forbidden,
                message = error.message).toResponse()
        )

        is RequestError.BadCredentials -> call.respond(
            HttpStatusCode.BadRequest,
            BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = error.message).toResponse()
        )

        is RequestError.BadRole -> call.respond(
            HttpStatusCode.Forbidden,
            BaseResponse.ErrorResponse(
                statusCode = HttpStatusCode.Forbidden,
                message = error.message).toResponse()
        )
    }
}

fun PipelineContext<Unit, ApplicationCall>.getUserId(): Long {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("userId").toString()
        .replace("\"", "").toLong()
}

fun PipelineContext<Unit, ApplicationCall>.getEmail(): String {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("email").toString()
        .replace("\"", "").toString()
}