package io.ashdavies.notion

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

private const val PAGE_ACTION_DESCRIPTION =
    "The Page object contains the property values of a single Notion page."

private const val PAGE_VIEW_DESCRIPTION =
    "Retrieves a Page object using the ID specified."

private const val PAGE_ID_DESCRIPTION =
    "Identifier for a Notion page"

@ExperimentalCli
internal class PageCommand(
    client: NotionClient,
    printer: Printer = Printer(),
) : CloseableSubcommand(
    actionDescription = PAGE_ACTION_DESCRIPTION,
    closeable = client,
    name = "user"
) {
    init {
        val retrieve = PageViewCommand(
            printer = printer,
            client = client,
        )

        subcommands(retrieve)
    }

    override suspend fun run() = Unit
}

@ExperimentalCli
private class PageViewCommand(
    private val client: NotionClient,
    private val printer: Printer,
) : CloseableSubcommand(
    actionDescription = PAGE_VIEW_DESCRIPTION,
    closeable = client,
    name = "view",
) {

    private val pageId: String by argument(
        description = PAGE_ID_DESCRIPTION,
        type = ArgType.String,
        fullName = "page_id",
    )

    override suspend fun run() {
        val results: List<NotionObject> = client
            .view(pageId)
            .results

        printer {
            results.forEachIndexed { _, it ->
                print { it.title[0].plainText }
            }
        }
    }
}
