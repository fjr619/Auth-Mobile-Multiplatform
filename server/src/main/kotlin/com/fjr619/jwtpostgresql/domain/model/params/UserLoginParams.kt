package com.fjr619.jwtpostgresql.domain.model.params

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginParams(
    val email: String,
    val password: String
)