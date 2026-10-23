package dev.ashdavies.playground.paging

import dev.ashdavies.http.callable.UnaryCallable
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.ashdavies.http.qualifier.AppCheckHttpClient
import dev.ashdavies.http.throwClientRequestExceptionAs
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.get
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

private const val NETWORK_PAGE_SIZE = 100

internal fun interface UpcomingEventsCallable : UnaryCallable<GetEventsRequest, Result<List<DomainWrapper<DomainEvent>>>>

@Serializable
internal data class GetEventsRequest(
    val startAt: LocalDate? = null,
    val limit: Int = NETWORK_PAGE_SIZE,
)

@Inject
@ContributesBinding(AppScope::class)
internal class ErrorHandlingUpcomingEventsCallable(
    @AppCheckHttpClient httpClient: HttpClient,
) : UpcomingEventsCallable {

    private val errorHandlingHttpClient = httpClient.config {
        install(HttpCallValidator) { throwClientRequestExceptionAs<GetEventsError>() }
        expectSuccess = true
    }

    override suspend fun invoke(request: GetEventsRequest): Result<List<DomainWrapper<DomainEvent>>> = runCatching {
        val queryAsString = buildList {
            if (request.startAt != null) add("startAt=${request.startAt}")
            add("limit=${request.limit}")
        }.joinToString("&")

        errorHandlingHttpClient
            .get("events/upcoming?$queryAsString")
            .body()
    }
}

@Serializable
internal data class GetEventsError(
    override val message: String,
    val code: Int,
) : Throwable()
