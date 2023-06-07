package io.ashdavies.playground

import io.ashdavies.notion.Notion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlin.collections.set
import kotlin.math.pow

private const val DEFAULT_SCORE = 1500.0
private const val K_FACTOR = 32L

private val SEARCH_DATABASES_FILTER = """
{
    "query": "Inbox",
    "filter": {
        "value": "database",
        "property": "object"
    },
    "page_size": 1
}
""".trimIndent()

private val SEARCH_PAGES_FILTER = """
{
    "filter": {
        "and": [
            {
                "property": "Status",
                "status": {
                    "equals": "Backlog"
                }
            },
            {
                "property": "Tags",
                "multi_select": {
                    "is_empty": true
                }
            }
        ]
    },
    "page_size": 3
}
""".trimIndent()

internal interface RatingsService : ItemPager<RatingsItem> {
    suspend fun rate(items: List<RatingsItem>)
    suspend fun ignore(item: RatingsItem)
}

internal fun RatingsService(client: HttpClient): RatingsService = object :
    ItemPager<RatingsItem> by ItemPager(RatingsItemGenerator(client)),
    RatingsService {

    private val registry = mutableMapOf<String, RatingsItem>()
    private var previous = emptyMap<String, RatingsItem>()

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
            registry[it.id] = it.copy(score = score)
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

private typealias ItemGenerator<T> = suspend CoroutineScope.() -> List<T>

private fun RatingsItemGenerator(client: HttpClient) = object : ItemGenerator<RatingsItem> {

    private lateinit var databaseId: String

    override suspend fun invoke(scope: CoroutineScope): List<RatingsItem> {
        if (!::databaseId.isInitialized) {
            val databaseList = client.post("search") {
                contentType(ContentType.Application.Json)
                setBody(SEARCH_DATABASES_FILTER)
            }.body<Notion.Object.Search>()

            databaseId = databaseList.results
                .run { first() as Notion.Object.Database }
                .id
        }

        val firstPage = client.post("databases/$databaseId/query") {
            contentType(ContentType.Application.Json)
            setBody(SEARCH_PAGES_FILTER)
        }.body<Notion.Object.Search>()

        return firstPage.results.map { page ->
            check(page is Notion.Object.Page)

            val name = page
                .let { it.properties["Name"] as Notion.Property.Title }
                .title[0]
                .plainText

            RatingsItem(
                id = page.id,
                name = name,
                ignored = false,
                score = DEFAULT_SCORE,
                url = page.url,
            )
        }
    }
}
