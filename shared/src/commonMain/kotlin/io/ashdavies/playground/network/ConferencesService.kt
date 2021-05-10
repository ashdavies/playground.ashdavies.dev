package io.ashdavies.playground.network

import io.ashdavies.playground.database.Conference
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API =
    "http://localhost:5001/playground-1a136/europe-west1/v1/conferences"

class ConferencesService(private val httpClient: HttpClient) {

    suspend fun get(token: String): List<Conference> =
        httpClient.get("$CONFERENCES_API?token=$token")
}
