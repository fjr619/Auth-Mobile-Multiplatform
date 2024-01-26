package com.fjr619.jwtpostgresql.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService: TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override fun verifier(config: TokenConfig): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(config.secret))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
    }
}