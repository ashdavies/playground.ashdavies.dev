package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceFactory
import io.ashdavies.playground.network.GitHub
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val ASG_CONFERENCES =
    "AndroidStudyGroup/conferences"

private const val CONFERENCES_API =
    "https://api.github.com/repos/$ASG_CONFERENCES/contents/_conferences"

private const val CONFERENCES_RAW =
    "https://raw.githubusercontent.com/$ASG_CONFERENCES/gh-pages/_conferences"

class ConferencesService(private val httpClient: HttpClient) {

    private val factory = ConferenceFactory { -1 }

    suspend fun getAll(): List<Conference> =
        httpClient
            .get<List<GitHub.Item>>(CONFERENCES_API)
            .map { factory(it.name) }

    suspend fun get(name: String): Conference =
        httpClient
            .get<GitHub.Item>("$CONFERENCES_API/$name")
            .let { raw(it.name) }

    private suspend fun raw(name: String): Conference =
        httpClient.get("$CONFERENCES_RAW/$name")
}
