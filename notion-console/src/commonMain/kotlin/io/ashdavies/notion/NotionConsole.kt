package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.ashdavies.playground.TokenQueries
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.NotionClient

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>) {
    var authentication by remember { mutableStateOf(Authentication()) }
    val client: NotionClient = rememberNotionClient(authentication)
    val queries: TokenQueries = rememberTokenQueries()

    LaunchedEffect(Unit) {
        authentication = queries
            .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
            .executeAsOneOrNull()
            ?: Authentication()
    }

    SearchCommand(client)
    AuthCommand()
}
