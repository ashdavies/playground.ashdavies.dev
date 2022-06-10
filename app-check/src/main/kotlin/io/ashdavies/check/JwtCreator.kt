package io.ashdavies.check

import com.auth0.jwt.JWTCreator
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

internal object PublicClaims : com.auth0.jwt.impl.PublicClaims {
    const val APP_ID = "app_id"
    const val TTL = "ttl"
}

internal fun JWTCreator.Builder.withAppId(appId: String): JWTCreator.Builder =
    withClaim(PublicClaims.APP_ID, appId)

internal fun JWTCreator.Builder.withExpiresAt(expiresAt: Instant): JWTCreator.Builder =
    withExpiresAt(java.util.Date.from(expiresAt.toJavaInstant()))

internal fun JWTCreator.Builder.withIssuedAt(issuedAt: Instant): JWTCreator.Builder =
    withIssuedAt(java.util.Date.from(issuedAt.toJavaInstant()))

internal fun JWTCreator.Builder.withTtl(ttlMillis: Int?): JWTCreator.Builder =
    if (ttlMillis != null) withTtl(ttlMillis) else this

// TODO Validate time representation with period validity of short lived token
internal fun JWTCreator.Builder.withTtl(ttlMillis: Int): JWTCreator.Builder =
    withClaim(PublicClaims.TTL, ttlMillis / 1000)
