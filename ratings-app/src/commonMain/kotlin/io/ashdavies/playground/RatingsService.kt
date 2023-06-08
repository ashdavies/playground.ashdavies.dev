package io.ashdavies.playground

import io.ashdavies.notion.Notion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.math.pow

private const val DEFAULT_SCORE = 1500.0
private const val K_FACTOR = 32L

private val SEARCH_DATABASES_FILTER = """
{
    "query": "%s",
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
            },
            {
                "or": [
                    {
                        "property": "Score",
                        "number": {
                            "is_empty": true
                        }
                    },
                    {
                        "property": "Score",
                        "number": {
                            "less_than": $DEFAULT_SCORE
                        }
                    }
                ]
            }
        ]
    },
    "page_size": 3
}
""".trimIndent()

private val UPDATE_PAGE_SCORE = """
{
    "properties": {
        "Score": {
            "number": %f
        }
    }
}
""".trimIndent()

private val Notion.Object.Page.title: String
    get() = (properties["Name"] as Notion.Property.Title)
        .title[0]
        .plainText

internal interface RatingsService : ItemPager<RatingsItem> {
    suspend fun rate(items: List<RatingsItem>)
    suspend fun ignore(item: RatingsItem)
}

internal fun RatingsService(client: HttpClient): RatingsService = object :
    ItemPager<RatingsItem> by ItemPager(RatingsItemGenerator(client)),
    RatingsService {

    private val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
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

            backgroundScope
                .launch { client.updatePageScore(it.id, score) }
                .join()
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
        if (!::databaseId.isInitialized) databaseId = client.getDatabaseId()

        return client.getDatabasePages(databaseId).map {
            RatingsItem(
                id = it.id,
                name = it.title,
                ignored = false,
                score = DEFAULT_SCORE,
                url = it.url,
            )
        }
    }
}

private suspend fun HttpClient.getDatabaseId(name: String = "Inbox"): String {
    val databaseList = post("search") {
        setBody(SEARCH_DATABASES_FILTER.format(name))
        contentType(ContentType.Application.Json)
    }.body<Notion.Object.Search>()

    return databaseList.results
        .run { first() as Notion.Object.Database }
        .id
}

private suspend fun HttpClient.getDatabasePages(id: String): List<Notion.Object.Page> {
    val pages = post("databases/$id/query") {
        contentType(ContentType.Application.Json)
        setBody(SEARCH_PAGES_FILTER)
    }.body<Notion.Object.Search>()

    return pages.results.map {
        it as Notion.Object.Page
    }
}

private suspend fun HttpClient.updatePageScore(id: String, value: Double) {
    patch("pages/$id") {
        contentType(ContentType.Application.Json)
        setBody(UPDATE_PAGE_SCORE.format(1500))
    }
}
