package dev.ashdavies.config.firebase.rest

import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.RemoteConfigValue
import dev.ashdavies.playground.metro.map
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.suspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes

private val RetryDelayInMillis = 1.minutes.inWholeMilliseconds

private const val RETRY_MAX_ATTEMPTS = 3L

public class FirebaseRestRemoteConfig(
    private val httpClient: HttpClient,
    private val environment: Environment,
    private val request: Request,
) : RemoteConfig {

    private val entries = suspendLazy {
        flow { emit(getEntries()) }
            .retry(RETRY_MAX_ATTEMPTS) {
                delay(RetryDelayInMillis)
                true
            }
            .catch {
                // TODO Log non fatal
                emit(emptyMap())
            }
            .first()
    }

    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return entries
            .map { transform(FirebaseRestRemoteConfigValue(it[key])) }
            .await()
    }

    private suspend fun getEntries(): Map<String, String> {
        val response = httpClient.post(
            urlString = "https://firebaseremoteconfig.googleapis.com" +
                "/v1/projects/${environment.projectId}/namespaces" +
                "/firebase:fetch?key=${environment.apiKey}",
        ) {
            header(HttpHeaders.Referrer, "http://localhost") // TODO: Remove
            setBody(request)
        }

        if (response.status != HttpStatusCode.OK) {
            throw IllegalStateException(response.bodyAsText())
        }

        return response
            .body<Response>()
            .entries
    }

    public data class Environment(val projectId: String, val apiKey: String)

    @Serializable
    public data class Request(val appId: String, val appInstanceId: String)

    @Serializable
    private data class Response(val entries: Map<String, String>)
}
