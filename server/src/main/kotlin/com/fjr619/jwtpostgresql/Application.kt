package com.fjr619.jwtpostgresql

import com.fjr619.jwtpostgresql.data.db.DatabaseFactory
import com.fjr619.jwtpostgresql.data.repository.auth.AuthRepositoryImpl
import com.fjr619.jwtpostgresql.presentation.plugin.configureExceptions
import com.fjr619.jwtpostgresql.presentation.plugin.configureSecurity
import com.fjr619.jwtpostgresql.presentation.plugin.configureSerialization
import com.fjr619.jwtpostgresql.domain.service.auth.AuthServiceImpl
import com.fjr619.jwtpostgresql.domain.repository.auth.AuthRepository
import com.fjr619.jwtpostgresql.presentation.routes.auth.authRoutes
import com.fjr619.jwtpostgresql.presentation.routes.user.userRoutes
import com.fjr619.jwtpostgresql.domain.service.security.hash.SHA256HashingService
import com.fjr619.jwtpostgresql.domain.service.security.token.JwtTokenService
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.hash.HashingService
import com.fjr619.jwtpostgresql.presentation.plugin.configureKoin
import com.fjr619.jwtpostgresql.presentation.plugin.configureRouting
import com.fjr619.jwtpostgresql.presentation.plugin.configureValidation
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

//fun main() {
//    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureExceptions()
    configureSecurity()
    configureValidation()
    configureRouting()
}
