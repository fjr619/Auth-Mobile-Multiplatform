package com.fjr619.jwtpostgresql.domain.service.security.hash

data class SaltedHash(
    val hash: String,
    val salt: String
)