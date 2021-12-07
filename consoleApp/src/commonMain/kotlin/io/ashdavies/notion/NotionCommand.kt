package io.ashdavies.notion

import io.ashdavies.playground.DatabaseFactory
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

private val AuthHistory.accessToken: String?
    get() = authResponseQueries
        .select()
        .executeAsOneOrNull()
        ?.accessToken

@OptIn(ExperimentalCli::class)
internal class NotionCommand(private val args: Array<String>) : ArgParser("notion") {

    init {
        runBlocking {
            val authorisationAdapter = Authorisation.Adapter(AuthResponseOwnerAdapter)
            val databaseFactory = DatabaseFactory()

            val authHistory = databaseFactory.create(AuthHistory.Schema) {
                AuthHistory(it, authorisationAdapter)
            }

            val uuidRegistrar = UuidRegistrar(databaseFactory)
            val notionClient: NotionClient = authHistory
                .accessToken
                ?.let { NotionClient(it) }
                ?: NotionClient()

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
}
