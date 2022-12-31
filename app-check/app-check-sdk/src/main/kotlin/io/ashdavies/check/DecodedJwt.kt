package io.ashdavies.check

import com.auth0.jwt.interfaces.DecodedJWT

internal fun DecodedJWT.asDecodedToken() = DecodedToken(
    expiresAt = expiresAtAsInstant.epochSecond,
    issuedAt = issuedAtAsInstant.epochSecond,
    audience = audience,
    subject = subject,
    issuer = issuer,
    appId = subject,
)
