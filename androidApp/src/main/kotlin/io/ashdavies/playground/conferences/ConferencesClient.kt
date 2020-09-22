package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.GitHub
import java.lang.System.currentTimeMillis

private const val UNKNOWN = "unknown"

internal class ConferencesClient(private val conferencesService: ConferencesService) {

    suspend fun getAll(): List<Conference> = conferencesService
        .getAll()
        .map(::resolve)

    private fun resolve(
        item: GitHub.Item<Conference>,
        time: Long = currentTimeMillis()
    ): Conference = Conference(
        name = item.name,
        website = item.url,
        location = UNKNOWN,
        dateStart = time,
        dateEnd = time,
        cfpStart = time,
        cfpEnd = time,
        cfpSite = UNKNOWN,
    )
}
