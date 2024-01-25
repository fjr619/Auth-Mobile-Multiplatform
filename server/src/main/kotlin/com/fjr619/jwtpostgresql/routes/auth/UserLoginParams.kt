package com.fjr619.jwtpostgresql.routes.auth

data class UserLoginParams(
    val email: String,
    val password: String
)