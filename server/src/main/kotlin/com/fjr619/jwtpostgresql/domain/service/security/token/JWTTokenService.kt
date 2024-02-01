package com.fjr619.jwtpostgresql.domain.service.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fjr619.jwtpostgresql.domain.model.entity.User
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.util.pipeline.PipelineContext
import org.koin.core.annotation.Singleton
import java.util.*

//https://www.youtube.com/watch?v=uezSuUQt6DY&t=8s
@Singleton
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

fun PipelineContext<Unit, ApplicationCall>.getUserId(): Long {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("userId").toString()
        .replace("\"", "").toLong()
}

fun PipelineContext<Unit, ApplicationCall>.getEmail(): String {
    return call.principal<JWTPrincipal>()?.payload?.getClaim("email").toString()
        .replace("\"", "").toString()
}
fun generateToken(
    tokenConfig: TokenConfig,
    tokenService: TokenService,
    userId: Long,
    email: String
): String {
    return tokenService.generate(
        config = tokenConfig,
        claims = arrayOf(
            TokenClaim(
                name = "userId",
                value = userId.toString()
            ),
            TokenClaim(
                name = "email",
                value = email
            )
        )
    )
}