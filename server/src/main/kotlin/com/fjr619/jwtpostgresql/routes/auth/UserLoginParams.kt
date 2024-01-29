package com.fjr619.jwtpostgresql.routes.auth

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginParams(
    val email: String,
    val password: String
)