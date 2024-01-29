package com.fjr619.jwtpostgresql.base.security.hash

data class SaltedHash(
    val hash: String,
    val salt: String
)