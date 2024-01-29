package com.fjr619.jwtpostgresql.presentation.routes.user

import com.fjr619.jwtpostgresql.base.BaseResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.userRoutes() {
    routing {
        authenticate {
            route("/user") {
                get {
                    call.respond(HttpStatusCode.OK,
                        BaseResponse.SuccessResponse("api user") as BaseResponse<String>
                    )
                }
            }
        }
    }
}