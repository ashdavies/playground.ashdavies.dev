@file:OptIn(ExperimentalCli::class)

package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.required
import org.jraf.klibnotion.client.Authentication

internal sealed interface NotionState {
    data object Initialising : NotionState
}

@Composable
@OptIn(ExperimentalCli::class)
internal fun NotionCli(args: Array<String>, onState: (NotionState) -> Unit) {
    ComposeCli("notion", args) {
        ProvidePlaygroundDatabase {
            val clientSecret by option(ArgType.String, "client_secret").required()
            val clientId by option(ArgType.String, "client_id").required()
            val authentication by produceAuthenticationState()
            val credentials = AuthCredentials(
                clientSecret = clientSecret,
                clientId = clientId,
            )

            NotionCompositionLocals(authentication, credentials) {
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
