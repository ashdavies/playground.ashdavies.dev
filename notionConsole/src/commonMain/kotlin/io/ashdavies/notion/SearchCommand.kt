package io.ashdavies.notion

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.sort.PropertySort

private const val SEARCH_ACTION_DESCRIPTION =
    "Searches all pages and child pages that are shared with the integration."

private const val SEARCH_QUERY_DESCRIPTION =
    "When supplied, limits which pages are returned by comparing the query to the page title."

private const val SORT_DIRECTION_DESCRIPTION =
    "When supplied, sorts the results based on the provided criteria."

private const val SORT_TIMESTAMP_DESCRIPTION =
    "The name of the timestamp to sort against."

private const val START_CURSOR_DESCRIPTION =
    "If supplied, this endpoint will return a page of results starting after the cursor provided."

@ExperimentalCli
internal class SearchCommand(
    private val client: NotionClient.Search,
) : CloseableSubcommand(
    actionDescription = SEARCH_ACTION_DESCRIPTION,
    closeable = Closeable { },
    name = "search",
) {

    private val query: String? by option(
        description = SEARCH_QUERY_DESCRIPTION,
        type = ArgType.String,
        fullName = "query",
    )

    private val sortDirection: SortDirection? by option(
        description = SORT_DIRECTION_DESCRIPTION,
        fullName = "sort_direction",
        type = ArgType.Choice(),
    )

    private val sortTimestamp: SortTimestamp? by option(
        description = SORT_TIMESTAMP_DESCRIPTION,
        fullName = "sort_timestamp",
        type = ArgType.Choice(),
    )

    private val startCursor: String? by option(
        description = START_CURSOR_DESCRIPTION,
        fullName = "start_cursor",
        type = ArgType.String,
    )

    override suspend fun run() {
        val page: ResultPage<Page> = client.searchPages(
            sort = sort(sortTimestamp, sortDirection),
            pagination = Pagination(startCursor),
            query = query,
        )

        val total = if (page.nextPagination != null) "many" else "${page.results.size}"
        println("Showing ${page.results.size} databases of $total\n\n")
        page.results.forEach { println(" ${it.url}\n") }
    }

    private fun sort(timestamp: SortTimestamp?, direction: SortDirection?): PropertySort? = timestamp?.let {
        when (direction) {
            SortDirection.ASCENDING -> PropertySort().ascending(it.name.lowercase())
            else -> PropertySort().descending(it.name.lowercase())
        }
    }
}


private enum class SortDirection {
    DESCENDING,
    ASCENDING,
}

private enum class SortTimestamp {
    LAST_EDITED_TIME,
}
