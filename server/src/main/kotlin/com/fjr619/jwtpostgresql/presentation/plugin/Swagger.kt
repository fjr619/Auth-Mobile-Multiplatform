package com.fjr619.jwtpostgresql.presentation.plugin

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.AuthScheme
import io.github.smiley4.ktorswaggerui.dsl.AuthType
import io.ktor.server.application.*
fun Application.configureSwagger() {
    // https://github.com/SMILEY4/ktor-swagger-ui/wiki/Configuration
    // http://xxx/swagger/

    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger"
            forwardRoot = false
        }

        info {
            title = "Ktor Auth Api Rest"
            version = "1.0.0"
            description = "Example of a Ktor Api Rest using Kotlin and Ktor"
            contact {
                name = "Franky Wijanarko"
                url = "https://github.com/fjr619"
            }
        }

        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }

        // We can add security
        securityScheme("JWT-Auth") {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
            bearerFormat = "jwt"
        }
    }
}