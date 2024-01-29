package com.fjr619.jwtpostgresql.base

import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Singleton

/**
 * Application Configuration to encapsulate our configuration
 * from application.conf or from other sources
 */
@Singleton
class AppConfig {
    val applicationConfiguration: ApplicationConfig = ApplicationConfig("application.conf")
}