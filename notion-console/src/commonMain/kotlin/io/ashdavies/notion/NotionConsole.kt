@file:OptIn(ExperimentalCli::class)

package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.ashdavies.notion.cli.LocalArgParser
import io.ashdavies.notion.compose.LocalNotionClient
import io.ashdavies.notion.compose.newInstance
import io.ashdavies.notion.compose.rememberTokenQueries
import io.ashdavies.playground.TokenQueries
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.NotionClient

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>) {
    var authentication by remember { mutableStateOf(Authentication()) }
    val tokenQueries: TokenQueries = rememberTokenQueries()

    LaunchedEffect(Unit) {
        authentication = tokenQueries
            .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
            .executeAsOneOrNull()
            ?: Authentication()
    }

    val notionClient: NotionClient = NotionClient.newInstance(authentication)
    val argParser: ArgParser = LocalArgParser.current

    CompositionLocalProvider(LocalNotionClient provides notionClient) {
        SearchCommand()
        AuthCommand()
    }

    LaunchedEffect(args) {
        argParser.parse(args)
    }
}
