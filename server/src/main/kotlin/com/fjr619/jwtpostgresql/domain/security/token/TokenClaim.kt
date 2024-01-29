package com.fjr619.jwtpostgresql.domain.security.token

data class TokenClaim(
    val name: String,
    val value: String
)