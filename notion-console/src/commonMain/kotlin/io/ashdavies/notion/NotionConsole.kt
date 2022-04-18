package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.ashdavies.playground.LocalPlaygroundDatabase
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>) {
    val playgroundDatabase = LocalPlaygroundDatabase.current

    LaunchedEffect(Unit) {
        val tokenQueries = playgroundDatabase.tokenQueries
        val argParser = ArgParser("notion")

        val authentication: Authentication = tokenQueries
            .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
            .executeAsOneOrNull()
            ?: Authentication()

        val clientConfiguration = ClientConfiguration(authentication)
        val notionClient = NotionClient.newInstance(clientConfiguration)

        val auth = AuthCommand(tokenQueries)
        val search = SearchCommand(notionClient.search)

        argParser.subcommands(auth, search)
        argParser.parse(args)
    }
}
