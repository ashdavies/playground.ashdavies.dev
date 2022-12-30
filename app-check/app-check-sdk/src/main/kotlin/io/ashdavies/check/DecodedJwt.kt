package io.ashdavies.check

import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.datetime.toKotlinInstant

internal fun DecodedJWT.asDecodedToken() = DecodedToken(
    expiresAt = expiresAtAsInstant.toKotlinInstant(),
    issuedAt = issuedAtAsInstant.toKotlinInstant(),
    audience = audience,
    subject = subject,
    issuer = issuer,
    appId = subject,
)
