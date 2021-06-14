package io.ashdavies.playground.network

import io.ashdavies.playground.database.Conference
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API =
    "https://europe-west1-playground-1a136.cloudfunctions.net/v1/conferences"

interface ConferencesService {
    suspend fun get(): List<Conference>
}

fun ConferencesService(httpClient: HttpClient) = object : ConferencesService {
    override suspend fun get(): List<Conference> = httpClient.get(CONFERENCES_API)
}