package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.oauth2.ComputeEngineCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.playground.cloud.LocalFirebaseApp

private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val ServiceAccountId: String? get() = System.getenv("SERVICE_ACCOUNT_ID")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")

internal val FirebaseApp.credentials: GoogleCredentials
    get() = FirebaseOptions::class.java
        .getDeclaredMethod("getCredentials")
        .also { it.isAccessible = true }
        .invoke(options) as GoogleCredentials


@Composable
internal fun rememberProjectId(app: FirebaseApp = LocalFirebaseApp.current): String = remember(app) {
    requireNotNull(findExplicitProjectId(app)) {
        "Failed to determine project ID. Initialize the " +
                "SDK with service account credentials or set project ID as an app option. " +
                "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
    }
}

@Composable
internal fun rememberServiceAccountId(app: FirebaseApp = LocalFirebaseApp.current): String = remember(app) {
    requireNotNull(findExplicitServiceAccountId(app)) { "Failed to determine service account identifier." }
}

private fun findExplicitProjectId(app: FirebaseApp): String? = app.options.projectId
    ?: (app.credentials as? ServiceAccountCredentials)?.projectId
    ?: GoogleCloudProject
    ?: GCloudProject

private fun findExplicitServiceAccountId(app: FirebaseApp): String? = app.options.serviceAccountId
    ?: (app.credentials as? ServiceAccountCredentials)?.account
    ?: (app.credentials as? ComputeEngineCredentials)?.account
    ?: ServiceAccountId
