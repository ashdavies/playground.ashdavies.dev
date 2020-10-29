@file:Suppress("FunctionName")

package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.network.GitHub
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val CONFERENCES_API =
    "https://api.github.com/repos/AndroidStudyGroup/conferences/contents/_conferences"

interface GitHubService<Key, Output> {

    suspend fun getAll(): List<GitHub.Item<Output>>

    suspend fun get(key: Key): GitHub.Item<Output>
}

private class HttpConferencesService(
    private val httpClient: HttpClient
) : GitHubService<String, Conference> {

    override suspend fun getAll(): List<GitHub.StandardItem<Conference>> =
        httpClient.get(CONFERENCES_API)

    override suspend fun get(key: String): GitHub.StandardItem<Conference> =
        httpClient.get("$CONFERENCES_API/$key")
}

fun <Key, Output> GitHubService(httpClient: HttpClient): GitHubService<String, Conference> =
    HttpConferencesService(httpClient)

suspend inline fun <Key, Input, Output> GitHubService<Key, Input>.getAll(
    transform: (GitHub.Item<Input>) -> Output
) = getAll().map { transform(it) }
