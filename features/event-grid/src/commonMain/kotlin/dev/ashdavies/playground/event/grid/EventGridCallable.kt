package dev.ashdavies.playground.event.grid

import dev.ashdavies.asg.PastConferencesCallable
import dev.ashdavies.cloud.toDomainEvent
import dev.ashdavies.http.callable.UnaryCallable
import dev.ashdavies.http.common.models.DomainEvent
import io.ktor.client.HttpClient

internal fun interface EventGridCallable : UnaryCallable<Unit, List<DomainEvent>>

internal fun EventGridCallable(httpClient: HttpClient): EventGridCallable {
    val asgCallable by lazy { PastConferencesCallable(httpClient) }

    return EventGridCallable { request ->
        asgCallable(request).map {
            it.toDomainEvent()
        }
    }
}
