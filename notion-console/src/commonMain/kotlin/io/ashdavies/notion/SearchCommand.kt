package io.ashdavies.notion

import androidx.compose.runtime.Composable
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.database.Database
import org.jraf.klibnotion.model.pagination.Pagination
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.sort.PropertySort

@Composable
@ExperimentalCli
@NotionScopeMarker
internal fun SearchCommand(
    notionClient: NotionClient = LocalNotionClient.current,
    onSearchState: (SearchState) -> Unit = { },
) = Subcommand(
    actionDescription = "Searches all pages and child pages that are shared with the integration.",
    onExecute = {
        val sortTimestamp: SortTimestamp? by it.option(
            description = "The name of the timestamp to sort against.",
            fullName = "sort_timestamp",
            type = ArgType.Choice(),
        )

        val sortDirection: SortDirection? by it.option(
            description = "Sorts the results based on the provided criteria.",
            fullName = "sort_direction",
            type = ArgType.Choice(),
        )

        val startCursor: String? by it.option(
            description = "Returns the results starting after the cursor provided.",
            fullName = "start_cursor",
            type = ArgType.String,
        )

        val query: String? by it.option(
            description = "Limits which pages are returned by comparing the query to the page title.",
            type = ArgType.String,
            fullName = "query",
        )

        val page: ResultPage<Database> = notionClient.search.searchDatabases(
            sort = sort(sortTimestamp, sortDirection),
            pagination = Pagination(startCursor),
            query = query,
        )

        onSearchState(SearchState.Complete(page.results))
    },
    name = "search",
)

internal sealed interface SearchState : NotionState {
    data class Complete(val value: List<Database>) : SearchState
}

private fun sort(
    timestamp: SortTimestamp?,
    direction: SortDirection?,
): PropertySort? = timestamp?.let {
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
