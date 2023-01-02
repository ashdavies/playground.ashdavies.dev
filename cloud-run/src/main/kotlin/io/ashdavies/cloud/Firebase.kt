package io.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

internal val firebaseApp: FirebaseApp
    get() = FirebaseApp.getApps()
        .firstOrNull { it.name == FirebaseApp.DEFAULT_APP_NAME }
        ?: initializeFirebaseApp()

private fun initializeFirebaseApp(): FirebaseApp {
    return when (val serviceAccountId = System.getenv("GOOGLE_SERVICE_ACCOUNT_ID")) {
        null -> FirebaseApp.initializeApp()
        else -> FirebaseApp.initializeApp(
            FirebaseOptions(serviceAccountId)
        )
    }
}

private fun FirebaseOptions(serviceAccountId: String): FirebaseOptions =
    FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setServiceAccountId(serviceAccountId)
        .build()
