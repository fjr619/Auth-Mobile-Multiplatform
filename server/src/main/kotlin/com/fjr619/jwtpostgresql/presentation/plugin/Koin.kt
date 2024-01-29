package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.di.appModule
import io.ktor.server.application.*
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger() // Logger
        defaultModule() // Default module with Annotations
        modules(appModule) // Our module, without dependencies
    }
}