package io.ashdavies.playground

import io.ashdavies.notion.NotionClient
import org.jraf.klibnotion.model.pagination.Pagination

private val NotionDatabase: String
    get() = requireNotNull(System.getenv("NOTION_DATABASE")) {
        "Notion database must be provided"
    }

private val NotionToken: String
    get() = requireNotNull(System.getenv("NOTION_TOKEN")) {
        "Notion token must be provided"
    }

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

internal class NotionRatingsService : RatingsService {

    private val client = NotionClient(NotionToken)
    private var pagination: Pagination? = Pagination()

    override suspend fun next(count: Int): List<RatingsItem> {
        val pages = client.databases.queryDatabase(
            pagination = pagination ?: return emptyList(),
            id = NotionDatabase,
        )

        pagination = pages.nextPagination

        return pages.results.map {
            RatingsItem(
                name = it.url,
                url = it.url,
                id = it.id,
                score = 0L,
            )
        }
    }
}

internal data class RatingsItem(
    val name: String,
    val score: Long,
    val url: String,
    val id: String,
)
