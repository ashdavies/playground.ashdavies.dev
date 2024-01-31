package io.ashdavies.random

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

internal fun interface RandomProvider {
    suspend fun getRandomUsers(size: Int): List<RandomUser>
}

internal fun RandomProvider(client: HttpClient) = RandomProvider { size ->
    client
        .get("https://randomuser.me/api/?results=$size")
        .body<Envelope<RandomUser>>()
        .results
}

internal suspend fun RandomProvider.getRandomUser(): RandomUser {
    return getRandomUsers(1).first()
}

@Serializable
private data class Envelope<T>(
    val results: List<T>,
)
