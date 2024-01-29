package com.fjr619.jwtpostgresql

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.plugin.configureExceptions
import com.fjr619.jwtpostgresql.plugin.configureSecurity
import com.fjr619.jwtpostgresql.plugin.configureSerialization
import com.fjr619.jwtpostgresql.data.repository.auth.AuthRepositoryImpl
import com.fjr619.jwtpostgresql.presentation.routes.auth.authRoutes
import com.fjr619.jwtpostgresql.presentation.routes.user.userRoutes
import com.fjr619.jwtpostgresql.base.security.hash.SHA256HashingService
import com.fjr619.jwtpostgresql.base.security.token.JwtTokenService
import com.fjr619.jwtpostgresql.base.security.token.TokenConfig
import com.fjr619.jwtpostgresql.data.service.auth.AuthServiceImpl
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

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
    userRoutes()
}
