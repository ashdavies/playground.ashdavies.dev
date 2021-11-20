package io.ashdavies.notion

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

private val AuthHistory.accessToken: String?
    get() = authResponseQueries
        .select()
        .executeAsOneOrNull()
        ?.accessToken

@OptIn(ExperimentalCli::class)
internal class NotionCommand(private val args: Array<String>) : ArgParser("notion") {

    private val authorisationAdapter = Authorisation.Adapter(AuthResponseOwnerAdapter)
    private val authHistory = DatabaseFactory.create(AuthHistory.Schema) {
        AuthHistory(it, authorisationAdapter)
    }

    private val uuidRegistrar = UuidRegistrar()
    private val notionClient: NotionClient =
        authHistory
            .accessToken
            ?.let { NotionClient(it) }
            ?: NotionClient()

    init {
        val auth = AuthCommand(
            queries = authHistory.authResponseQueries,
            client = notionClient,
        )

        val block = BlockCommand(
            registrar = uuidRegistrar,
            client = notionClient,
        )

        val page = PageCommand(
            registrar = uuidRegistrar,
            client = notionClient,
        )

        val search = SearchCommand(
            registrar = uuidRegistrar,
            client = notionClient,
        )

        subcommands(auth, block, page, search)
        parse(args)
    }
}
