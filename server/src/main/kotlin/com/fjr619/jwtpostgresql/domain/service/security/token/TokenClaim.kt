package com.fjr619.jwtpostgresql.domain.service.security.token

data class TokenClaim(
    val name: String,
    val value: String
)