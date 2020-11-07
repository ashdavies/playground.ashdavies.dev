@file:Suppress("FunctionName")

package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.Fetcher
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceService

internal fun ConferencesFetcher(
    conferenceService: ConferenceService,
): Fetcher<Any, List<Conference>> = Fetcher.of {
    conferenceService
        .getAll()
        .documents
        .map { it.content }
}
