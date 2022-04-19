package io.ashdavies.notion

import androidx.compose.runtime.Composable
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.sort.PropertySort

private const val SEARCH_ACTION_DESCRIPTION = "Searches all pages and child pages that are shared with the integration."
private const val SEARCH_QUERY_DESCRIPTION = "Limits which pages are returned by comparing the query to the page title."
private const val SORT_DIRECTION_DESCRIPTION = "Sorts the results based on the provided criteria."
private const val SORT_TIMESTAMP_DESCRIPTION = "The name of the timestamp to sort against."
private const val START_CURSOR_DESCRIPTION = "Returns the results starting after the cursor provided."

@Composable
@ExperimentalCli
internal fun SearchCommand(client: NotionClient) = rememberSubcommand("search", SEARCH_ACTION_DESCRIPTION) {
    val sortTimestamp by it.choice<SortTimestamp>("sort_timestamp", SORT_TIMESTAMP_DESCRIPTION)
    val sortDirection by it.choice<SortDirection>("sort_direction", SORT_DIRECTION_DESCRIPTION)
    val startCursor by it.string("start_cursor", START_CURSOR_DESCRIPTION)
    val query by it.string("query", SEARCH_QUERY_DESCRIPTION)

    val page: ResultPage<Database> = client.search.searchDatabases(
        sort = sort(sortTimestamp, sortDirection),
        pagination = Pagination(startCursor),
        query = query,
    )

    val total = if (page.nextPagination != null) "many" else "${page.results.size}"
    println("Showing ${page.results.size} databases of $total\n\n")
    page.results.forEach { println(" $it\n") }
}

private fun sort(timestamp: SortTimestamp?, direction: SortDirection?): PropertySort? = timestamp?.let {
    when (direction) {
        SortDirection.ASCENDING -> PropertySort().ascending(it.name.lowercase())
        else -> PropertySort().descending(it.name.lowercase())
    }
}

private enum class SortDirection {
    DESCENDING,
    ASCENDING,
}

private enum class SortTimestamp {
    LAST_EDITED_TIME,
}
