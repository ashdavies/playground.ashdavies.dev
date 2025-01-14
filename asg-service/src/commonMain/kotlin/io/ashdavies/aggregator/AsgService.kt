package io.ashdavies.aggregator

import io.ashdavies.aggregator.callable.PastConferencesCallable
import io.ashdavies.aggregator.callable.UpcomingConferencesCallable
import io.ashdavies.http.invoke
import io.ktor.client.HttpClient

private const val ASG_BASE_URL = "androidstudygroup.github.io"

public interface AsgService {
    public suspend operator fun <T : Any> invoke(transform: (AsgConference) -> T): List<T>
}

public fun AsgService(httpClient: HttpClient): AsgService = object : AsgService {
    override suspend operator fun <T : Any> invoke(transform: (AsgConference) -> T): List<T> {
        val upcoming = UpcomingConferencesCallable(httpClient, ASG_BASE_URL)
        val past = PastConferencesCallable(httpClient, ASG_BASE_URL)
        val combined = upcoming() + past().reversed()
        return combined.map(transform)
    }
}

public fun UpcomingConferencesCallable(httpClient: HttpClient): UpcomingConferencesCallable {
    return UpcomingConferencesCallable(httpClient, ASG_BASE_URL)
}

public fun PastConferencesCallable(httpClient: HttpClient): PastConferencesCallable {
    return PastConferencesCallable(httpClient, ASG_BASE_URL)
}
