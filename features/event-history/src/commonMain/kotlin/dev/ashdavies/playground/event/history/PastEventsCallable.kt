package dev.ashdavies.playground.event.history

import dev.ashdavies.http.callable.UnaryCallable
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.ashdavies.http.qualifier.AuthenticatedHttpClient
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

// TODO Sync with database with background job
@Inject
internal class PastEventsCallable(
    @AuthenticatedHttpClient private val httpClient: HttpClient,
) : UnaryCallable<Unit, Result<List<DomainWrapper<DomainEvent>>>> {

    override suspend fun invoke(request: Unit) = runCatching<List<DomainWrapper<DomainEvent>>> {
        httpClient
            .get("events/past")
            .body()
    }
}
