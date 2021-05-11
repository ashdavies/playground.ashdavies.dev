package io.ashdavies.playground.conferences

import com.dropbox.android.external.store4.Fetcher
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.network.ConferencesService

internal fun ConferencesFetcher(
    githubToken: String,
    conferencesService: ConferencesService,
): Fetcher<Any, List<Conference>> = Fetcher.of {
    conferencesService.get(githubToken)
}
