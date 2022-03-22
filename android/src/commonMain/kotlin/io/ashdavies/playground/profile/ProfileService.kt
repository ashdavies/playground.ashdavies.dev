package io.ashdavies.playground.profile

import io.ashdavies.playground.network.Service
import io.ashdavies.playground.network.ServiceOperator
import io.ashdavies.playground.network.serviceOperator
import io.ktor.client.HttpClient
import io.ktor.http.content.OutgoingContent.NoContent
import kotlinx.serialization.Serializable

public interface ProfileService : Service {
    val createAuthUri: ServiceOperator<NoContent, String>
    val lookup: ServiceOperator<Lookup.Request, Lookup.Response>
}

public fun profileService(httpClient: HttpClient, endpoint: String, apiKey: String) = object : ProfileService {
    override val createAuthUri by serviceOperator<NoContent, String>(httpClient) { "$endpoint/$it?key=$apiKey" }
    override val lookup by serviceOperator<Lookup.Request, Lookup.Response>(httpClient) { "$endpoint/$it?key=$apiKey" }
}

public sealed class Lookup {
    @Serializable data class Request(val lookupId: String) : Lookup()
    @Serializable data class Response(val email: String) : Lookup()
}
