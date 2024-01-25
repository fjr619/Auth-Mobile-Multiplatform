package com.fjr619.jwtpostgresql

import Greeting
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("AAAA: ${Greeting().greet()}")
        }
    }
}