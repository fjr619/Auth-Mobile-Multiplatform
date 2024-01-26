package com.fjr619.jwtpostgresql

import SERVER_PORT
import com.fjr619.jwtpostgresql.db.DatabaseFactory
import com.fjr619.jwtpostgresql.plugin.configureExceptions
import com.fjr619.jwtpostgresql.plugin.configureSecurity
import com.fjr619.jwtpostgresql.plugin.configureSerialization
import com.fjr619.jwtpostgresql.repository.auth.AuthRepositoryImpl
import com.fjr619.jwtpostgresql.routes.auth.authRoutes
import com.fjr619.jwtpostgresql.security.hash.SHA256HashingService
import com.fjr619.jwtpostgresql.security.token.JwtTokenService
import com.fjr619.jwtpostgresql.security.token.TokenConfig
import com.fjr619.jwtpostgresql.service.auth.AuthServiceImpl
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.EngineMain
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

//fun main() {
//    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureExceptions()

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = environment.config.property("jwt.secret").getString()
    )
    val hashingService = SHA256HashingService()
    val authService = AuthServiceImpl(hashingService)
    val authRepository = AuthRepositoryImpl(authService, tokenService, tokenConfig)
    configureSecurity(tokenService, tokenConfig)
    authRoutes(authRepository)
}
