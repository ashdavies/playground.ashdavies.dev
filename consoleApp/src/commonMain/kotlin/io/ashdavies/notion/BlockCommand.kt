package io.ashdavies.notion

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

private const val BLOCK_ACTION_DESCRIPTION =
    "A block object represents content within Notion. Blocks can be text, lists, media, and more."

private const val BLOCK_CHILDREN_DESCRIPTION =
    "Retrieve block children."

private const val BLOCK_ID_DESCRIPTION =
    "Identifier for a block."

private const val START_CURSOR_DESCRIPTION =
    "If supplied, this endpoint will return a page of results starting after the cursor provided."

private const val PAGE_SIZE_DESCRIPTION =
    "The number of items from the full list desired in the response (Maximum: 100)."

@ExperimentalCli
internal class BlockCommand(
    client: NotionClient,
    registrar: UuidRegistrar,
    printer: Printer = Printer(),
) : CloseableSubcommand(
    actionDescription = BLOCK_ACTION_DESCRIPTION,
    closeable = client,
    name = "block",
) {

    init {
        val children = BlockChildrenCommand(
            client = client,
            registrar = registrar,
            printer = printer,
        )

        subcommands(children)
    }

    override suspend fun run() = Unit
}

@ExperimentalCli
private class BlockChildrenCommand(
    private val client: NotionClient,
    private val registrar: UuidRegistrar,
    private val printer: Printer,
) : CloseableSubcommand(
    actionDescription = BLOCK_CHILDREN_DESCRIPTION,
    closeable = client,
    name = "view",
) {

    private val blockId: String by argument(
        description = BLOCK_ID_DESCRIPTION,
        type = ArgType.String,
        fullName = "block_id",
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
        TODO("Not yet implemented")
    }
}
