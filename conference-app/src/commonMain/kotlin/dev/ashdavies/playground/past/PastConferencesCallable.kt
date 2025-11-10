package dev.ashdavies.playground.past

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.asg.PastConferencesCallable
import dev.ashdavies.cloud.Identifier
import dev.ashdavies.cloud.toApiConference
import dev.ashdavies.http.UnaryCallable
import dev.ashdavies.http.common.models.ApiConference
import io.ktor.client.HttpClient

internal fun interface PastConferencesCallable : UnaryCallable<Unit, List<ApiConference>>

internal fun PastConferencesCallable(httpClient: HttpClient): PastConferencesCallable {
    val asgCallable by lazy { PastConferencesCallable(httpClient) }
    val identifier = Identifier<AsgConference>()

    return PastConferencesCallable { request ->
        asgCallable(request).map {
            it.toApiConference(identifier(it))
        }
    }
}
