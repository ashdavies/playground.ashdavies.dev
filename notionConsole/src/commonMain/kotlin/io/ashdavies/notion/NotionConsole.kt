package io.ashdavies.notion

import androidx.compose.runtime.Composable
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

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>) = runBlocking {
    val argParser = ArgParser("notion")
    val authResponseQueries: AuthResponseQueries = DatabaseFactory()
        .create(AuthHistory.Schema, AuthHistory.Companion::invoke)
        .authResponseQueries

    val authentication: Authentication = authResponseQueries
        .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
        .executeAsOneOrNull()
        ?: Authentication()

    val clientConfiguration = ClientConfiguration(authentication)
    val notionClient = NotionClient.newInstance(clientConfiguration)

    val auth = AuthCommand(notionClient.oAuth, authResponseQueries)
    val search = SearchCommand(notionClient.search)

    argParser.subcommands(auth, search)
    argParser.parse(args)
}
