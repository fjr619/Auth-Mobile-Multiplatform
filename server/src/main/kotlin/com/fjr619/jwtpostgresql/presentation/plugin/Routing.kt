package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.presentation.routes.user.userRoutes
import io.ktor.server.application.Application

/**
 * Define the routing of our application based a DSL
 * https://ktor.io/docs/routing-in-ktor.html
 * we can define our routes in separate files like routes package
 */
fun Application.configureRouting() {
    userRoutes()
}