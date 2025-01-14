package io.ashdavies.aggregator.callable

import io.ashdavies.aggregator.AsgConference
import io.ashdavies.http.UnaryCallable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public fun interface PastConferencesCallable : UnaryCallable<Unit, List<AsgConference>>

internal fun PastConferencesCallable(
    httpClient: HttpClient,
    baseUrl: String,
) = PastConferencesCallable { _ ->
    httpClient
        .get("https://$baseUrl/conferences/past.json")
        .body()
}
