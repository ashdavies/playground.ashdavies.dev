package io.ashdavies.check

internal object AppCheckConstants {

    const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"
    const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

    const val APP_CHECK_AUDIENCE = "${APP_CHECK_ENDPOINT}google.firebase.appcheck.v1.TokenExchangeService"
    const val APP_CHECK_PUBLIC_KEY = "${APP_CHECK_ENDPOINT}v1/jwks"
    const val APP_CHECK_V1_API = "${APP_CHECK_ENDPOINT}v1/projects"

    const val APP_CHECK_TOKEN_KEY = "APP_CHECK_TOKEN"

    val FIREBASE_CLAIMS_SCOPES = listOf(
        "https://www.googleapis.com/auth/cloud-platform",
        "https://www.googleapis.com/auth/firebase.database",
        "https://www.googleapis.com/auth/firebase.messaging",
        "https://www.googleapis.com/auth/identitytoolkit",
        "https://www.googleapis.com/auth/userinfo.email",
    )
}
