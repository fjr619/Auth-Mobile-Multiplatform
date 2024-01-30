package com.fjr619.jwtpostgresql.presentation.error

import com.fjr619.jwtpostgresql.base.BaseResponse
import com.fjr619.jwtpostgresql.domain.model.UserError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.handleUserError(
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