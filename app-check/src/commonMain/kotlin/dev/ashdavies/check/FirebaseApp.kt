package dev.ashdavies.check

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import java.net.UnknownHostException

public fun FirebaseApp.appCheck(httpClient: HttpClient): AppCheck = AppCheck(
    cryptoSigner = CryptoSigner(this),
    projectId = getProjectId(this),
    httpClient = httpClient,
)

internal fun getProjectId(firebaseApp: FirebaseApp): String = requireNotNull(findExplicitProjectId(firebaseApp)) {
    "Failed to determine project ID. Initialize the " +
        "SDK with service account credentials or set project ID as an app option. " +
        "Alternatively, set the GOOGLE_CLOUD_PROJECT environment variable."
}

private fun findExplicitProjectId(firebaseApp: FirebaseApp): String? = firebaseApp.options.projectId
    ?: googleCredentials<ServiceAccountCredentials, String?> { it.projectId }
    ?: BuildConfig.GOOGLE_CLOUD_PROJECT
    ?: BuildConfig.GCLOUD_PROJECT
    ?: BuildConfig.GCP_PROJECT
    ?: fetchProjectId()

private fun findExplicitProjectNumber(): String? = BuildConfig.APP_ID
    ?.let { it.split(":")[1] }
    ?: fetchProjectNumber()

private inline fun <reified T : GoogleCredentials, R> googleCredentials(transform: (T) -> R): R? {
    return (GoogleCredentials.getApplicationDefault() as? T)?.let(transform)
}

private fun fetchProjectId(): String? = fetchComputeMetadataBlocking(path = "project/project-id")

private fun fetchProjectNumber(): String? = fetchComputeMetadataBlocking(path = "project/numeric-project-id")

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
