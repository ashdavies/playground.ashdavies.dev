package io.ashdavies.check

import com.google.auth.oauth2.ComputeEngineCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.internal.CryptoSigners
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.runBlocking
import java.net.UnknownHostException

private val GoogleServiceAccountId: String? get() = System.getenv("GOOGLE_SERVICE_ACCOUNT_ID")
private val GoogleCloudProject: String? get() = System.getenv("GOOGLE_CLOUD_PROJECT")
private val MobileSdkAppId: String? get() = System.getenv("MOBILE_SDK_APP_ID")
private val GCloudProject: String? get() = System.getenv("GCLOUD_PROJECT")
private val GCPProject: String? get() = System.getenv("GCP_PROJECT")

public fun FirebaseApp.appCheck(httpClient: HttpClient): AppCheck = AppCheck(
    cryptoSigner = CryptoSigner(this, httpClient),
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

internal fun getServiceAccountId(firebaseApp: FirebaseApp): String =
    requireNotNull(findExplicitServiceAccountId(firebaseApp)) {
        "Failed to determine service account. Make sure to initialize " +
            "the SDK with a service account credential. Alternatively specify a service " +
            "account with iam.serviceAccounts.signBlob permission."
    }

private fun findExplicitProjectId(firebaseApp: FirebaseApp): String? =
    firebaseApp.options.projectId
        ?: googleCredentials<ServiceAccountCredentials, String?> { it.projectId }
        ?: GoogleCloudProject
        ?: GCloudProject
        ?: GCPProject
        ?: fetchProjectId()

private fun findExplicitProjectNumber(): String? =
    MobileSdkAppId
        ?.let { it.split(":")[1] }
        ?: fetchProjectNumber()

private fun findExplicitServiceAccountId(firebaseApp: FirebaseApp): String? =
    firebaseApp.options.serviceAccountId
        ?: googleCredentials<ServiceAccountCredentials, String?> { it.account }
        ?: googleCredentials<ComputeEngineCredentials, String?> { it.account }
        ?: GoogleServiceAccountId
        ?: fetchServiceAccountId()

private inline fun <reified T : GoogleCredentials, R> googleCredentials(transform: (T) -> R): R? =
    (GoogleCredentials.getApplicationDefault() as? T)?.let(transform)

private fun fetchProjectId(): String? =
    fetchComputeMetadataBlocking(path = "project/project-id")

private fun fetchProjectNumber(): String? =
    fetchComputeMetadataBlocking(path = "project/numeric-project-id")

private fun fetchServiceAccountId(): String? =
    fetchComputeMetadataBlocking(path = "instance/service-accounts/default/email")

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
