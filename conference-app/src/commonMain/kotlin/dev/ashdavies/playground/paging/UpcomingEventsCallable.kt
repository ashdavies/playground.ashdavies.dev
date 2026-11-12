package dev.ashdavies.playground.paging

import dev.ashdavies.http.callable.UnaryCallable
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.ashdavies.http.qualifier.AuthenticatedHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

private const val NETWORK_PAGE_SIZE = 100

internal typealias UpcomingEventsCallable = UnaryCallable<UpcomingEventsRequest, Result<UpcomingEventsResponse>>
internal typealias UpcomingEventsResponse = List<DomainWrapper<DomainEvent>>

@Inject
@ContributesBinding(AppScope::class)
internal class AuthenticatedUpcomingEventsCallable(
    @AuthenticatedHttpClient private val httpClient: HttpClient,
) : UpcomingEventsCallable {

    override suspend fun invoke(request: UpcomingEventsRequest) = runCatching<UpcomingEventsResponse> {
        val queryAsString = buildList {
            if (request.startAt != null) add("startAt=${request.startAt}")
            add("limit=${request.limit}")
        }.joinToString("&")

        httpClient
            .get("events/upcoming?$queryAsString")
            .body()
    }
}

@Serializable
internal data class UpcomingEventsRequest(
    val startAt: LocalDate? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)
