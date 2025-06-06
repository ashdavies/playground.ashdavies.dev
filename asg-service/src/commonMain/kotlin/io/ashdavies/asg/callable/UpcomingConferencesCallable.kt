package io.ashdavies.asg.callable

import io.ashdavies.asg.AsgConference
import io.ashdavies.http.UnaryCallable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public fun interface UpcomingConferencesCallable : UnaryCallable<Unit, List<AsgConference>>

internal fun UpcomingConferencesCallable(
    httpClient: HttpClient,
    baseUrl: String,
) = UpcomingConferencesCallable { _ ->
    httpClient
        .get("https://$baseUrl/conferences/upcoming.json")
        .body()
}
