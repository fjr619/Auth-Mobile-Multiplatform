package com.fjr619.jwtpostgresql.presentation.routes.auth

import com.fjr619.jwtpostgresql.domain.model.params.CreateUserParams
import com.fjr619.jwtpostgresql.domain.model.params.UserLoginParams
import com.fjr619.jwtpostgresql.domain.service.auth.AuthService
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.authRoutes() {

    val authService: AuthService by inject()

    routing {
        route("/auth") {
            post("/register") {
                val params = call.receive<CreateUserParams>()
                val result = authService.registeruser(params)
                call.respond(result.statusCode, result)
            }

            post("/login") {
                val params = call.receive<UserLoginParams>()
                val result = authService.loginUser(params)
                call.respond(result.statusCode, result)
            }
        }
    }
}