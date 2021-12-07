package io.ashdavies.notion

import androidx.compose.runtime.Composable
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

private val AuthHistory.accessToken: String?
    get() = authResponseQueries
        .select()
        .executeAsOneOrNull()
        ?.accessToken

@Composable
internal fun NotionCommand(args: Array<String>) {

}

@OptIn(ExperimentalCli::class)
internal class NotionCommand2(private val args: Array<String>) : ArgParser("notion") {

    init {
        runBlocking {
            val authorisationAdapter = Authorisation.Adapter(ColumnAdapter())
            val databaseFactory = DatabaseFactory()

            val authHistory = databaseFactory.create(AuthHistory.Schema) {
                AuthHistory(it, authorisationAdapter)
            }

            val notionClient: NotionClient = authHistory
                .accessToken
                ?.let { NotionClient(it) }
                ?: NotionClient()

            val auth = AuthCommand(
                queries = authHistory.authResponseQueries,
                client = notionClient,
            )

            val block = BlockCommand(
                client = notionClient,
            )

            val page = PageCommand(
                client = notionClient,
            )

            val search = SearchCommand(
                client = notionClient,
            )

            subcommands(auth, block, page, search)
            parse(args)
        }
    }
}
