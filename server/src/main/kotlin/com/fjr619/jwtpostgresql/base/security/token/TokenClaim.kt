package com.fjr619.jwtpostgresql.base.security.token

data class TokenClaim(
    val name: String,
    val value: String
)