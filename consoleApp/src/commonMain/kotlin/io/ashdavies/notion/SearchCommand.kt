package io.ashdavies.notion

import io.ashdavies.notion.SearchQuery.Filter
import io.ashdavies.notion.SearchQuery.Filter.Property
import io.ashdavies.notion.SearchQuery.Sort
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

private const val SEARCH_ACTION_DESCRIPTION =
    "Searches all pages and child pages that are shared with the integration."

private const val SEARCH_QUERY_DESCRIPTION =
    "When supplied, limits which pages are returned by comparing the query to the page title."

private const val SORT_DIRECTION_DESCRIPTION =
    "When supplied, sorts the results based on the provided criteria."

private const val SORT_TIMESTAMP_DESCRIPTION =
    "The name of the timestamp to sort against."

private const val FILTER_VALUE_DESCRIPTION =
    "The value of the property to filter the results by."

private const val FILTER_PROPERTY_DESCRIPTION =
    "The name of the property to filter by."

private const val START_CURSOR_DESCRIPTION =
    "If supplied, this endpoint will return a page of results starting after the cursor provided."

private const val PAGE_SIZE_DESCRIPTION =
    "The number of items from the full list desired in the response (Maximum: 100)."

@ExperimentalCli
internal class SearchCommand(
    private val client: NotionClient,
    private val registrar: UuidRegistrar,
    private val printer: Printer = Printer(),
) : CloseableSubcommand(
    actionDescription = SEARCH_ACTION_DESCRIPTION,
    closeable = client,
    name = "search",
) {

    private val query: String? by option(
        description = SEARCH_QUERY_DESCRIPTION,
        type = ArgType.String,
        fullName = "query",
    )

    private val sortDirection: Sort.Direction? by option(
        description = SORT_DIRECTION_DESCRIPTION,
        fullName = "sort_direction",
        type = ArgType.Choice(),
    )

    private val sortTimestamp: Sort.Timestamp? by option(
        description = SORT_TIMESTAMP_DESCRIPTION,
        fullName = "sort_timestamp",
        type = ArgType.Choice(),
    )

    private val filterValue: NotionType? by option(
        description = FILTER_VALUE_DESCRIPTION,
        fullName = "filter_value",
        type = ArgType.Choice(),
    )

    private val filterProperty: Property? by option(
        description = FILTER_PROPERTY_DESCRIPTION,
        fullName = "filter_property",
        type = ArgType.Choice(),
    )

    private val startCursor: String? by option(
        description = START_CURSOR_DESCRIPTION,
        fullName = "start_cursor",
        type = ArgType.String,
    )

    private val pageSize: Int? by option(
        description = PAGE_SIZE_DESCRIPTION,
        fullName = "page_size",
        type = ArgType.Int,
    )

    override suspend fun run() {
        val search = SearchQuery(
            query = query,
            sort = sortTimestamp?.let { Sort(it, sortDirection) },
            filter = filterProperty?.let { Filter(it, filterValue!!) },
            startCursor = startCursor,
            pageSize = pageSize,
        )

        val page: NotionPage<NotionObject> = client.search(search)
        val results: List<NotionObject> = page.results
        val uuid: List<UuidValue> = results
            .map { UuidValue.fromString(it.id) }
            .onEach { registrar.register(it) }

        printer {
            val total = if (page.hasMore) "many" else "${results.size}"
            print { "Showing ${results.size} databases of $total\n\n" }

            results.forEachIndexed { index, it ->
                green { uuid[index].short }
                print { " ${it.title[0].plainText}\n" }
            }
        }
    }
}
