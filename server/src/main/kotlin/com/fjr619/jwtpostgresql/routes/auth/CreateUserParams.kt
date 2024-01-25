package com.fjr619.jwtpostgresql.routes.auth

data class CreateUserParams(
    val fullName: String,
    val email: String,
    val password: String,
    val avatar: String
)