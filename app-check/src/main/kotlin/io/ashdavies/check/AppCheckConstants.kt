package io.ashdavies.check

internal object AppCheckConstants {

    private const val FIREBASE_APP_CHECK_BASE = "https://firebaseappcheck.googleapis.com"

    const val APP_CHECK_AUDIENCE = "$FIREBASE_APP_CHECK_BASE/google.firebase.appcheck.v1.TokenExchangeService"
    const val APP_CHECK_ISSUER = "$FIREBASE_APP_CHECK_BASE/"
    const val APP_CHECK_PUBLIC_KEY = "$FIREBASE_APP_CHECK_BASE/v1beta/jwks"

    const val APP_CHECK_TOKEN_KEY = "APP_CHECK_TOKEN"

    const val FIREBASE_APP_CHECK_V1_API_ENDPOINT = "$FIREBASE_APP_CHECK_BASE/v1/projects"
    const val GOOGLE_TOKEN_AUDIENCE = "https://accounts.google.com/o/oauth2/token";

    private const val GOOGLE_SCOPE_BASE = "https://www.googleapis.com/auth"

    val FIREBASE_CLAIMS_SCOPES = listOf(
        "$GOOGLE_SCOPE_BASE/cloud-platform",
        "$GOOGLE_SCOPE_BASE/firebase.database",
        "$GOOGLE_SCOPE_BASE/firebase.messaging",
        "$GOOGLE_SCOPE_BASE/identitytoolkit",
        "$GOOGLE_SCOPE_BASE/userinfo.email",
    )
}
