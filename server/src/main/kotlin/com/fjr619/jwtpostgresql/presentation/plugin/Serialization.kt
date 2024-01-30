package com.fjr619.jwtpostgresql.presentation.plugin

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fjr619.jwtpostgresql.base.HttpStatusCodeSerializer
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

/**
 * Configure the serialization of our application based on JSON
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
//        jackson {
//            configure(SerializationFeature.INDENT_OUTPUT, true)
//            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
//                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
//                indentObjectsWith(DefaultIndenter("  ", "\n"))
//            })
//        }

        json(Json {
            encodeDefaults = true
            prettyPrint = true
            isLenient = true
            serializersModule = SerializersModule {
                contextual(HttpStatusCode::class) { HttpStatusCodeSerializer }
            }
        })
    }
}