package io.ashdavies.playground.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val BASE_API =
    "https://firestore.googleapis.com/v1"

private const val CONFERENCES_API =
    "$BASE_API/projects/playground-1a136/databases/(default)/documents/conferences"

private const val CONFERENCES_QUERY =
    "pageSize=20&orderBy=dateStart%20desc"

class ConferenceService(
    private val httpClient: HttpClient,
) : FirestoreService<Conference> {

    override suspend fun getAll(): FirestoreCollection<Conference> =
        httpClient.get("$CONFERENCES_API?$CONFERENCES_QUERY")

    override suspend fun get(key: String): FirestoreDocument<Conference> =
        httpClient.get("$CONFERENCES_API/$key?$CONFERENCES_QUERY")
}
