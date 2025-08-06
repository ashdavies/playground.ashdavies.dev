package io.ashdavies.tally.past

import dev.ashdavies.asg.PastConferencesCallable
import io.ashdavies.cloud.ApiConferenceFactory
import io.ashdavies.cloud.Identifier
import io.ashdavies.http.UnaryCallable
import io.ashdavies.http.common.models.ApiConference
import io.ktor.client.HttpClient

internal fun interface PastConferencesCallable : UnaryCallable<Unit, List<ApiConference>>

internal fun PastConferencesCallable(httpClient: HttpClient): PastConferencesCallable {
    val asgCallable by lazy { PastConferencesCallable(httpClient) }
    val apiConferenceFactory = ApiConferenceFactory(Identifier())

    return PastConferencesCallable { request ->
        asgCallable(request).map(apiConferenceFactory::invoke)
    }
}
