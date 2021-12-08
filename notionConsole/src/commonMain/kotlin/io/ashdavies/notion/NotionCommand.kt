package io.ashdavies.notion

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

private val AuthHistory.accessToken: String?
    get() = authResponseQueries
        .select()
        .executeAsOneOrNull()
        ?.accessToken

@OptIn(ExperimentalCli::class)
internal class NotionCommand(private val args: Array<String>) : ArgParser("notion") {

    init {
        runBlocking {
            val authResponseQueries: AuthResponseQueries = DatabaseFactory()
                .create(AuthHistory.Schema, AuthHistory.Companion::invoke)
                .authResponseQueries

            val authentication: Authentication =  authResponseQueries
                .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
                .executeAsOneOrNull()
                ?: Authentication()

            val clientConfiguration = ClientConfiguration(authentication)
            val notionClient = NotionClient.newInstance(clientConfiguration)

            val auth = AuthCommand(
                queries = authResponseQueries,
                client = notionClient.oAuth,
            )

            val search = SearchCommand(
                client = notionClient.search,
            )

            subcommands(auth, search)
            parse(args)
        }
    }
}
