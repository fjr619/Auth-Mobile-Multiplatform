package com.fjr619.jwtpostgresql.base.security.token

import com.auth0.jwt.JWTVerifier

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    fun verifier(config: TokenConfig): JWTVerifier
}