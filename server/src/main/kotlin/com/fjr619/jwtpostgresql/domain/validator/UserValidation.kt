package com.fjr619.jwtpostgresql.domain.validator

import com.fjr619.jwtpostgresql.domain.model.params.UserLoginParams
import com.fjr619.jwtpostgresql.domain.model.params.UserRegisterParams
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.userValidation() {
    validate<UserRegisterParams> { user ->
        if (user.fullName.isBlank()){
            ValidationResult.Invalid("The name cannot be empty")
        } else if (user.avatar.isBlank()) {
            ValidationResult.Invalid("The avatar cannot be empty")
        } else if (user.email.isBlank()) {
            ValidationResult.Invalid("The email cannot be empty")
        } else if (user.password.isBlank()) {
            ValidationResult.Invalid("The password cannot be empty")
        } else {
            ValidationResult.Valid
        }
    }

    validate<UserLoginParams> { user ->
        if (user.email.isBlank()) {
            ValidationResult.Invalid("The email cannot be empty")
        } else if (user.password.isBlank()) {
            ValidationResult.Invalid("The password cannot be empty")
        } else {
            ValidationResult.Valid
        }
    }
}