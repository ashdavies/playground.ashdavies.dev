package io.ashdavies.check

import com.google.auth.oauth2.ComputeEngineCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking

private val GoogleServiceAccountId: String? get() = System.getenv("GOOGLE_SERVICE_ACCOUNT_ID")
private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")
private val GCPProject: String? get() = System.getenv("GCP_PROJECT")

public fun FirebaseApp.appCheck(httpClient: HttpClient, appId: String): AppCheck = AppCheck(
    projectId = getProjectId(this),
    httpClient = httpClient,
    firebaseApp = this,
    appId = appId,
)

internal fun getProjectId(app: FirebaseApp): String = requireNotNull(findExplicitProjectId(app)) {
    "Failed to determine project ID. Initialize the " +
            "SDK with service account credentials or set project ID as an app option. " +
            "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
}

internal fun getServiceAccountId(app: FirebaseApp): String =
    requireNotNull(findExplicitServiceAccountId(app)) {
        "Failed to determine service account. Make sure to initialize " +
                "the SDK with a service account credential. Alternatively specify a service " +
                "account with iam.serviceAccounts.signBlob permission."
    }

private fun findExplicitProjectId(app: FirebaseApp): String? = app.options.projectId
    ?: googleCredentials<ServiceAccountCredentials, String?> { it.projectId }
    ?: GoogleCloudProject
    ?: GCloudProject
    ?: GCPProject
    ?: fetchProjectId()

private fun findExplicitServiceAccountId(app: FirebaseApp): String? = app.options.serviceAccountId
    ?: googleCredentials<ServiceAccountCredentials, String?> { it.account }
    ?: googleCredentials<ComputeEngineCredentials, String?> { it.account }
    ?: GoogleServiceAccountId
    ?: fetchServiceAccountId()

private inline fun <reified T : GoogleCredentials, R> googleCredentials(transform: (T) -> R): R? {
    return (GoogleCredentials.getApplicationDefault() as? T)?.let(transform)
}

private fun fetchProjectId(httpClient: HttpClient = HttpClient()): String = runBlocking {
    fetchComputeMetadata(httpClient, "project/project-id")
}

private fun fetchServiceAccountId(httpClient: HttpClient = HttpClient()): String = runBlocking {
    fetchComputeMetadata(httpClient, "instance/service-accounts/default/email")
}

private suspend inline fun <reified T> fetchComputeMetadata(client: HttpClient, path: String): T {
    return client.get("http://metadata/computeMetadata/v1/$path") {
        header("Metadata-Flavor", "Google")
    }.body()
}
