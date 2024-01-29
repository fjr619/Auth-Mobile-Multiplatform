package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.domain.security.token.TokenConfig
import com.fjr619.jwtpostgresql.domain.security.token.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

fun Application.configureSecurity(tokenService: TokenService, config: TokenConfig) {
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