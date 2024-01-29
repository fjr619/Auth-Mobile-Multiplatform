package com.fjr619.jwtpostgresql.presentation.routes.auth

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginParams(
    val email: String,
    val password: String
)