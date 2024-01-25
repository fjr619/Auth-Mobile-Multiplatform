package com.fjr619.jwtpostgresql

import SERVER_PORT
import com.fjr619.jwtpostgresql.db.DatabaseFactory
import com.fjr619.jwtpostgresql.plugin.configureExceptions
import com.fjr619.jwtpostgresql.plugin.configureSerialization
import com.fjr619.jwtpostgresql.repository.auth.AuthRepositoryImpl
import com.fjr619.jwtpostgresql.routes.auth.authRoutes
import com.fjr619.jwtpostgresql.security.hash.SHA256HashingService
import com.fjr619.jwtpostgresql.service.auth.AuthServiceImpl
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureExceptions()

    val hashingService = SHA256HashingService()
    val authService = AuthServiceImpl(hashingService)
    val authRepository = AuthRepositoryImpl(authService)

    authRoutes(authRepository)
}
