package dev.ashdavies.config.firebase.rest

import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.RemoteConfigValue
import dev.ashdavies.sql.Suspended
import dev.ashdavies.sql.invoke
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

public class FirebaseRestRemoteConfig(
    private val httpClient: HttpClient,
    private val environment: Environment,
    private val request: Request,
) : RemoteConfig {

    private val entries = Suspended {
        httpClient
            .post(
                urlString = "https://firebaseremoteconfig.googleapis.com" +
                    "/v1/projects/${environment.projectId}/namespaces" +
                    "/firebase:fetch?key=${environment.apiKey}",
                block = { setBody(request) },
            )
            .body<Response>()
            .entries
    }

    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return entries { transform(FirebaseRestRemoteConfigValue(it[key])) }
    }

    public data class Environment(val projectId: String, val apiKey: String)

    @Serializable
    public data class Request(val appId: String, val appInstanceId: String)

    @Serializable
    private data class Response(val entries: Map<String, String>)
}
