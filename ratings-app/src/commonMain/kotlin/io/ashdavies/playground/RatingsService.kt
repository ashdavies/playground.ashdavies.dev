package io.ashdavies.playground

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

private const val GITHUB_ZEN = "https://api.github.com/zen"

internal fun interface RatingsService {
    suspend fun next(count: Int): List<RatingsItem>
}

internal suspend fun <T> RatingsService.next(count: Int, transform: (RatingsItem) -> T): List<T> {
    return next(count).map(transform)
}

internal suspend fun RatingsService.next(): RatingsItem {
    return next(1).first()
}

internal fun RatingsService(client: HttpClient) = RatingsService { size ->
    buildList {
        repeat(size) {
            val response = client.get(GITHUB_ZEN)
            val item = RatingsItem(
                id = randomUuid(),
                name = response.bodyAsText(),
                url = GITHUB_ZEN,
                score = 1500L,
            )

            add(item)
        }
    }
}
