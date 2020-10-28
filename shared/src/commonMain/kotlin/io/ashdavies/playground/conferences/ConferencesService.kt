package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.ConferenceFactory
import io.ashdavies.playground.network.GitHub
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API =
    "https://api.github.com/repos/AndroidStudyGroup/conferences/contents/_conferences"

class ConferencesService(private val httpClient: HttpClient) {

    private val factory = ConferenceFactory { -1 }

    suspend fun getAll(): List<Conference> =
        httpClient
            .get<List<GitHub.Item>>(CONFERENCES_API)
            .map { factory(it.name) }

    suspend fun get(name: String): GitHub.Item =
        httpClient.get("$CONFERENCES_API/$name")
}
