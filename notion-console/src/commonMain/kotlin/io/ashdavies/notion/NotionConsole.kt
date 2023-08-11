@file:OptIn(ExperimentalCli::class)

package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.Authentication

internal sealed interface NotionState {
    data object Initialising : NotionState
}

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionCli(args: Array<String>, onState: (NotionState) -> Unit) {
    ProvidePlaygroundDatabase {
        val authentication by produceAuthenticationState()

        ProvideNotionClient(authentication) {
            ComposeCli("notion", args) {
                SearchCommand { onState(it) }
                AuthCommand { onState(it) }
            }
        }
    }
}

@Composable
private fun produceAuthenticationState(
    initialValue: Authentication = Authentication(),
    database: PlaygroundDatabase = LocalPlaygroundDatabase.current,
): State<Authentication> = produceState(initialValue) {
    database.tokenQueries
        .select { accessToken, _, _, _, _ -> Authentication(accessToken) }
        .executeAsOneOrNull()
        ?: initialValue
}
