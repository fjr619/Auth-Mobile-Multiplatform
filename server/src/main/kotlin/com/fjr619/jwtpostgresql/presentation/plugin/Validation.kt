package com.fjr619.jwtpostgresql.presentation.plugin

import com.fjr619.jwtpostgresql.domain.validator.storyValidation
import com.fjr619.jwtpostgresql.domain.validator.userValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

/**
 * Configure the validation plugin
 * https://ktor.io/docs/request-validation.html
 * We extend the validation with our own rules in separate file in validators package
 * like routes
 */
fun Application.configureValidation() {
    install(RequestValidation) {
        userValidation()
        storyValidation()
    }
}