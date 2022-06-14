package io.ashdavies.check

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.check.AppCheckConstants.APP_CHECK_AUDIENCE
import io.ashdavies.check.AppCheckConstants.APP_CHECK_ISSUER
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.time.Duration.Companion.hours

internal object Jwt : JWT()

internal object PublicClaims : com.auth0.jwt.impl.PublicClaims {
    const val APP_ID = "app_id"
    const val SCOPE = "scope"
}

internal fun Jwt.create(algorithm: Algorithm, config: JwtOptions.() -> Unit = {}): String {
    return with(JwtOptions().apply(config)) {
        JWT.create()
            .withClaim(PublicClaims.APP_ID, appId)
            .withExpiresAt(expiresAt.toJavaDate())
            .withIssuedAt(issuedAt.toJavaDate())
            .withScopeIfNotEmpty(scope)
            .withAudience(audience)
            .withSubject(issuer)
            .withIssuer(issuer)
            .sign(algorithm)
    }
}

internal fun Jwt.verify(algorithm: Algorithm, token: String, config: JwtOptions.() -> Unit = { }): DecodedJWT {
    return with(JwtOptions().apply(config)) {
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

        verifier.verify(token)
    }
}

internal class JwtOptions(
    var audience: String = APP_CHECK_AUDIENCE,
    var issuedAt: Instant = Clock.System.now(),
    var expiresAt: Instant = issuedAt + 1.hours,
    var issuer: String = APP_CHECK_ISSUER,
    var scope: List<String> = emptyList(),
) {
    lateinit var appId: String
}

private fun Instant.toJavaDate() = java.util.Date.from(toJavaInstant())

private fun JWTCreator.Builder.withScopeIfNotEmpty(list: List<*>): JWTCreator.Builder {
    return if (list.isNotEmpty()) withClaim(PublicClaims.SCOPE, list.joinToString(separator = " ")) else this
}
