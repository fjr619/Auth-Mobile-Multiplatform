package com.fjr619.jwtpostgresql.security.token

data class TokenClaim(
    val name: String,
    val value: String
)