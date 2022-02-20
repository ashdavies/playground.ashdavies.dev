package io.ashdavies.notion

import androidx.compose.runtime.Composable
import io.ashdavies.playground.LocalPlaygroundDatabase
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>) {
    val argParser = ArgParser("notion")

    val playgroundDatabase = LocalPlaygroundDatabase.current
    val tokenQueries = playgroundDatabase.tokenQueries

    val authentication: Authentication = tokenQueries
        .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
        .executeAsOneOrNull()
        ?: Authentication()

    val clientConfiguration = ClientConfiguration(authentication)
    val notionClient = NotionClient.newInstance(clientConfiguration)

    val auth = AuthCommand(notionClient.oAuth, tokenQueries)
    val search = SearchCommand(notionClient.search)

    argParser.subcommands(auth, search)
    argParser.parse(args)
}
