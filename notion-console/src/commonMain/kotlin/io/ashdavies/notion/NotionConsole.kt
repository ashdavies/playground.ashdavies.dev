@file:OptIn(ExperimentalCli::class)

package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionConsole(args: Array<String>, onState: (NotionState) -> Unit) {
    val argParser = LocalArgParser.current

    ProvidePlaygroundDatabase {
        val database = LocalPlaygroundDatabase.current
        val authentication by produceState(Authentication()) {
            database.tokenQueries
                .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
                .executeAsOneOrNull()
                ?: Authentication()
        }

        ProvideNotionClient(authentication) {
            SearchCommand { onState(it) }
            AuthCommand { onState(it) }
        }
    }

    LaunchedEffect(args) {
        argParser.parse(args)
    }
}

internal sealed interface NotionState {
    object Initialising : NotionState
}
