package com.fjr619.jwtpostgresql.routes.auth

import com.fjr619.jwtpostgresql.repository.auth.AuthRepository
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.authRoutes(repository: AuthRepository) {
    routing {
        route("/auth") {
            post("/register") {
                val params = call.receive<CreateUserParams>()
                val result = repository.registeruser(params)
                call.respond(result.statusCode, result)
            }

            post("/login") {
                val params = call.receive<UserLoginParams>()
                val result = repository.loginUser(params)
                call.respond(result.statusCode, result)
            }
        }
    }
}