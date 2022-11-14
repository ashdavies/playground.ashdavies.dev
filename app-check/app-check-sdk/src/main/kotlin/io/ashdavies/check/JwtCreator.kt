package io.ashdavies.check

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.time.Duration.Companion.hours

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com"
private const val APP_CHECK_AUDIENCE = "${APP_CHECK_ENDPOINT}/google.firebase.appcheck.v1.TokenExchangeService"

internal object Jwt : JWT()

internal object PrivateClaims {
    const val APP_ID = "app_id"
    const val SCOPE = "scope"
}

internal fun Jwt.create(algorithm: Algorithm, config: (JwtOptions) -> Unit = {}): String {
    return with(JwtOptions().also(config)) {
        JWT.create()
            .withClaim(PrivateClaims.APP_ID, appId)
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

public class JwtOptions(
    public var audience: String = APP_CHECK_AUDIENCE,
    public var issuedAt: Instant = Clock.System.now(),
    public var expiresAt: Instant = issuedAt + 1.hours,
    public var scope: List<String> = emptyList(),
) {
    public lateinit var issuer: String
    public lateinit var appId: String
}

private fun Instant.toJavaDate() = java.util.Date.from(toJavaInstant())

private fun JWTCreator.Builder.withScopeIfNotEmpty(list: List<*>): JWTCreator.Builder {
    return if (list.isNotEmpty()) withClaim(PrivateClaims.SCOPE, list.joinToString(separator = " ")) else this
}
