package io.ashdavies.aggregator.callable

import io.ashdavies.aggregator.AsgConference
import io.ashdavies.http.UnaryCallable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class UpcomingConferencesCallable(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : UnaryCallable<Unit, List<AsgConference>> {

    override suspend fun invoke(request: Unit): List<AsgConference> = httpClient
        .get("https://$baseUrl/conferences/upcoming.json")
        .body()
}
