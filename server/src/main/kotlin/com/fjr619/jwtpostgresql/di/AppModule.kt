package com.fjr619.jwtpostgresql.di

import com.fjr619.jwtpostgresql.base.AppConfig
import com.fjr619.jwtpostgresql.domain.service.security.token.TokenConfig
import mu.KotlinLogging
import org.koin.dsl.module

val appModule = module {
    factory { TokenConfig(
        (get() as AppConfig).applicationConfiguration.propertyOrNull("jwt.issuer")?.getString() ?: "h2",
        (get() as AppConfig).applicationConfiguration.propertyOrNull("jwt.audience")?.getString() ?: "h2",
        365L * 1000L * 60L * 60L * 24L,
        (get() as AppConfig).applicationConfiguration.propertyOrNull("jwt.secret")?.getString() ?: "h2"
    ) }
    single {
        KotlinLogging.logger {}
    }
}