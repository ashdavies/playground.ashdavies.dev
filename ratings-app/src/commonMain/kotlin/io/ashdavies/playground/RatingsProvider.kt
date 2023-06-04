package io.ashdavies.playground

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

private const val GITHUB_ZEN = "https://api.github.com/zen"

internal interface RatingsProvider {
    fun vote(items: List<RatingsItem>)
    fun ignore(item: RatingsItem)
}

internal fun RatingsProvider() = object : RatingsProvider {
    override fun vote(items: List<RatingsItem>) = Unit
    override fun ignore(item: RatingsItem) = Unit
}

internal fun interface RatingsService {
    suspend fun next(count: Int): List<RatingsItem>
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

internal data class RatingsItem(
    val id: String,
    val name: String,
    val url: String,
    val score: Long,
)

internal expect fun randomUuid(): String
