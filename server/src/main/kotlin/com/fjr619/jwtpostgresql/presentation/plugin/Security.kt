package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.get as koinGet

fun Application.configureSecurity() {

    // We can also use Koin get() no lazy-loading, we use a alias to avoid conflicts with Ktor get()
    val tokenService: TokenService = koinGet()

    //dependency injection by Koin lazy-loading
    val config: TokenConfig by inject()

    authentication {
        jwt {
            verifier(tokenService.verifier(config))
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}