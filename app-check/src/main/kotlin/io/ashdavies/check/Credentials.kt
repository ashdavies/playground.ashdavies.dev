package io.ashdavies.check

import androidx.compose.runtime.Composable
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ashdavies.playground.cloud.LocalFirebaseApp

private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")

@Composable
internal fun getProjectId(): String = requireNotNull(findExplicitProjectId()) {
    "Failed to determine project ID. " +
            "Initialize the SDK with service account credentials or set project ID as an app option. " +
            "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
}

@Composable
private fun findExplicitProjectId(app: FirebaseApp = LocalFirebaseApp.current): String? = app.options.projectId
    ?: (app.credentials as? ServiceAccountCredentials)?.projectId
    ?: GoogleCloudProject
    ?: GCloudProject
