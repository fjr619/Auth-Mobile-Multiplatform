package com.fjr619.jwtpostgresql.domain.model.params

import kotlinx.serialization.Serializable


@Serializable
data class CreateUserParams(
    val fullName: String,
    val email: String,
    val password: String,
    val avatar: String
)