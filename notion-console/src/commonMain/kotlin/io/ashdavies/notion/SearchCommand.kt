package io.ashdavies.notion

import androidx.compose.runtime.Composable
import io.ashdavies.notion.cli.Subcommand
import io.ashdavies.notion.compose.LocalNotionClient
import kotlinx.cli.ArgType
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
internal fun SearchCommand(client: NotionClient = LocalNotionClient.current) {
    Subcommand("search", SEARCH_ACTION_DESCRIPTION, onExecute = {
        val sortTimestamp: SortTimestamp? by it.option(
            shortName = SORT_TIMESTAMP_DESCRIPTION,
            fullName = "sort_timestamp",
            type = ArgType.Choice(),
        )

        val sortDirection: SortDirection? by it.option(
            shortName = SORT_DIRECTION_DESCRIPTION,
            fullName = "sort_direction",
            type = ArgType.Choice(),
        )

        val startCursor: String? by it.option(
            shortName = START_CURSOR_DESCRIPTION,
            fullName = "start_cursor",
            type = ArgType.String,
        )

        val query: String? by it.option(
            shortName = SEARCH_QUERY_DESCRIPTION,
            type = ArgType.String,
            fullName = "query",
        )

        val page: ResultPage<Database> = client.search.searchDatabases(
            sort = sort(sortTimestamp, sortDirection),
            pagination = Pagination(startCursor),
            query = query,
        )

        val total = if (page.nextPagination != null) "many" else "${page.results.size}"
        println("Showing ${page.results.size} databases of $total\n\n")
        page.results.forEach { println(" $it\n") }
    })
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
