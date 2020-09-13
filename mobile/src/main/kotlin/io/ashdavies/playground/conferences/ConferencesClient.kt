package io.ashdavies.playground.conferences

import io.ashdavies.playground.github.GitHub
import io.ashdavies.playground.network.Conference
import java.util.Date

private const val UNKNOWN = "unknown"

internal class ConferencesClient(private val conferencesService: ConferencesService) {

    suspend fun getAll(): List<Conference> = conferencesService
        .getAll()
        .map(::resolve)

    private fun resolve(item: GitHub.Item<Conference>): Conference = Conference(
        name = item.name,
        website = item.url,
        location = UNKNOWN,
        dateStart = Date(),
        dateEnd = Date(),
        cfpStart = Date(),
        cfpEnd = Date(),
        cfpSite = UNKNOWN,
    )
}
