package io.ashdavies.check

import com.google.auth.oauth2.ComputeEngineCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

private val GoogleServiceAccountId: String? get() = System.getenv("GOOGLE_SERVICE_ACCOUNT_ID")
private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")

internal val FirebaseApp.credentials: GoogleCredentials
    get() = FirebaseOptions::class.java
        .getDeclaredMethod("getCredentials")
        .also { it.isAccessible = true }
        .invoke(options) as GoogleCredentials

public fun getProjectId(app: FirebaseApp): String = requireNotNull(findExplicitProjectId(app)) {
    "Failed to determine project ID. Initialize the " +
            "SDK with service account credentials or set project ID as an app option. " +
            "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
}

internal fun getServiceAccountId(app: FirebaseApp): String = requireNotNull(findExplicitServiceAccountId(app)) {
    "Failed to determine service account. Make sure to initialize " +
            "the SDK with a service account credential. Alternatively specify a service " +
            "account with iam.serviceAccounts.signBlob permission."
}

private fun findExplicitProjectId(app: FirebaseApp): String? = app.options.projectId
    ?: (app.credentials as? ServiceAccountCredentials)?.projectId
    ?: GoogleCloudProject
    ?: GCloudProject

private fun findExplicitServiceAccountId(app: FirebaseApp): String? = app.options.serviceAccountId
    ?: (app.credentials as? ServiceAccountCredentials)?.account
    ?: (app.credentials as? ComputeEngineCredentials)?.account
    ?: GoogleServiceAccountId
