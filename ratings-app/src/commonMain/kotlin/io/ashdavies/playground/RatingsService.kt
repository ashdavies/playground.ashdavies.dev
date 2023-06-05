package io.ashdavies.playground

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val GITHUB_ZEN = "https://api.github.com/zen"

internal fun interface RatingsService {
    suspend fun next(count: Int): List<RatingsItem>
}

internal fun RatingsService(client: HttpClient) = RatingsService { size ->
    withContext(Dispatchers.IO) {
        List(size) {
            RatingsItem(
                id = randomUuid(),
                name = client
                    .get(GITHUB_ZEN)
                    .bodyAsText(),
                url = GITHUB_ZEN,
                score = 1500L,
            )
        }
    }
}
