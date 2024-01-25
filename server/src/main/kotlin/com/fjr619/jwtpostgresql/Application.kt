package com.fjr619.jwtpostgresql

import SERVER_PORT
import com.fjr619.jwtpostgresql.db.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
}
