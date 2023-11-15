package io.ashdavies.check

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import java.net.UnknownHostException

private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")
private val GCPProject: String? get() = System.getenv("GCP_PROJECT")

public fun FirebaseApp.appCheck(httpClient: HttpClient): AppCheck = AppCheck(
    cryptoSigner = CryptoSigner(this),
    projectId = getProjectId(this),
    projectNumber = getProjectNumber(),
    httpClient = httpClient,
)

internal fun getProjectId(firebaseApp: FirebaseApp): String =
    requireNotNull(findExplicitProjectId(firebaseApp)) {
        "Failed to determine project ID. Initialize the " +
            "SDK with service account credentials or set project ID as an app option. " +
            "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
    }

internal fun getProjectNumber(): String =
    requireNotNull(findExplicitProjectNumber()) {
        "Failed to determine project number. Make sure to initialize " +
            "the SDK on a Google Cloud Compute Engine with default VM metadata. " +
            "Alternatively, set the GCP_PROJECT_NUMBER environment variable."
    }

private fun findExplicitProjectId(firebaseApp: FirebaseApp): String? =
    firebaseApp.options.projectId
        ?: googleCredentials<ServiceAccountCredentials, String?> { it.projectId }
        ?: GoogleCloudProject
        ?: GCloudProject
        ?: GCPProject
        ?: fetchProjectId()

private fun findExplicitProjectNumber(): String? =
    System.getProperty("firebase.android.app.id")
        ?.let { it.split(":")[1] }
        ?: fetchProjectNumber()

private inline fun <reified T : GoogleCredentials, R> googleCredentials(transform: (T) -> R): R? =
    (GoogleCredentials.getApplicationDefault() as? T)?.let(transform)

private fun fetchProjectId(): String? =
    fetchComputeMetadataBlocking(path = "project/project-id")

private fun fetchProjectNumber(): String? =
    fetchComputeMetadataBlocking(path = "project/numeric-project-id")

private inline fun <reified T> fetchComputeMetadataBlocking(
    httpClient: HttpClient = HttpClient(),
    path: String,
): T? = runBlocking {
    try {
        httpClient
            .get("http://metadata/computeMetadata/v1/$path") {
                header("Metadata-Flavor", "Google")
            }
            .body()
    } catch (ignored: UnknownHostException) {
        null
    }
}
