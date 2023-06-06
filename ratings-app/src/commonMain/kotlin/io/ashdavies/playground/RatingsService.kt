package io.ashdavies.playground

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.pow

private const val GENERATOR_URL = "https://whatthecommit.com/index.txt"
private const val DEFAULT_SCORE = 1500L
private const val K_FACTOR = 32L

internal interface RatingsService {
    suspend fun next(count: Int): List<RatingsItem>
    suspend fun rate(items: List<RatingsItem>)
    suspend fun ignore(item: RatingsItem)
}

internal fun RatingsService(client: HttpClient): RatingsService = object : RatingsService {

    private val registry = mutableMapOf<String, RatingsItem>()
    private var previous = emptyMap<String, RatingsItem>()
    private val messages = mutableListOf<String>()

    override suspend fun next(count: Int): List<RatingsItem> = withContext(Dispatchers.IO) {
        List(count) { RatingsItem(message()) }
    }

    private suspend fun message(): String {
        var backoff = 10L

        while (true) {
            val text = client
                .get(GENERATOR_URL)
                .bodyAsText()

            if (text !in messages) {
                messages += text
                return text
            }

            backoff *= 2
            delay(backoff)
        }
    }

    override suspend fun rate(items: List<RatingsItem>) {
        println("\n=== RatingService Vote ===")
        items.forEach { println("${it.id}: ${it.name} [${it.score}]") }

        val total = items.sumOf { 10.0.pow(it.score / 400.0) }
        println("\n=== RatingsService Calculation (Total: $total) ===")

        items.forEachIndexed { index, it ->
            val expected = 10.0.pow(it.score / 400.0) / total
            val actual = (1.0 / items.size) * (items.size - index)

            val score = it.score + (K_FACTOR * (actual - expected))
            val uuid = it.id.take(5)

            println("$uuid: { index = $index, expected = $expected, actual = $actual, score = $score })")
            registry[it.id] = it.copy(score = score.toLong())
        }

        print()
    }

    override suspend fun ignore(item: RatingsItem) {
        registry[item.id] = item.copy(ignored = true)
        println("Ignored: ${item.id}")
        print()
    }

    private fun print() {
        println("\n=== RatingsService Summary ===")

        val sorted = registry.values.sortedByDescending { it.score }

        sorted.forEach {
            val change = it.score - (previous[it.id]?.score ?: DEFAULT_SCORE)
            val text = when {
                change > 0 -> "$change 🔺"
                change < 0 -> "$change 🔻"
                else -> "⚪️ No Change"
            }

            println("${it.id}: ${it.name} [${it.score}: $text]")
        }

        previous = registry.toMap()
        println("\n")
    }
}

private fun RatingsItem(name: String) = RatingsItem(
    id = randomUuid(),
    name = name,
    ignored = false,
    score = DEFAULT_SCORE,
    url = GENERATOR_URL,
)
