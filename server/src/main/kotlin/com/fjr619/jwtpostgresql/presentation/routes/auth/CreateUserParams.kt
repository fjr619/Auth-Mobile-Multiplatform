package com.fjr619.jwtpostgresql.presentation.routes.auth

import kotlinx.serialization.Serializable


@Serializable
data class CreateUserParams(
    val fullName: String,
    val email: String,
    val password: String,
    val avatar: String
)