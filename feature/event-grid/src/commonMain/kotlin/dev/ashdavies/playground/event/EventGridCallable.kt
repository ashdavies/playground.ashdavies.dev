package dev.ashdavies.playground.event

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.asg.PastConferencesCallable
import dev.ashdavies.cloud.Identifier
import dev.ashdavies.cloud.toApiConference
import dev.ashdavies.http.UnaryCallable
import dev.ashdavies.http.common.models.ApiConference
import io.ktor.client.HttpClient

internal fun interface EventGridCallable : UnaryCallable<Unit, List<ApiConference>>

internal fun EventGridCallable(httpClient: HttpClient): EventGridCallable {
    val asgCallable by lazy { PastConferencesCallable(httpClient) }
    val identifier = Identifier<AsgConference>()

    return EventGridCallable { request ->
        asgCallable(request).map {
            it.toApiConference(identifier(it))
        }
    }
}
