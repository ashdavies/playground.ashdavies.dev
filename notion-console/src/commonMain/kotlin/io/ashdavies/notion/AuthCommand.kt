package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import io.ashdavies.playground.TokenQueries
import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@Composable
@ExperimentalCli
@NotionScopeMarker
internal fun ArgParser.AuthCommand(
    authCredentials: AuthCredentials = LocalAuthCredentials.current,
    tokenQueries: TokenQueries = rememberTokenQueries(),
    uriHandler: UriHandler = LocalUriHandler.current,
    onAuthState: (AuthState) -> Unit = { },
) = Subcommand(
    actionDescription = "Run notion auth login to authenticate with your Notion account.",
    name = "auth",
) {
    Subcommand(
        actionDescription = "Run notion auth login to authenticate with your Notion account.",
        onExecute = {
            val token = tokenQueries.select().executeAsOneOrNull()
            if (token != null) {
                onAuthState(AuthState.Authenticated)
            } else {
                onAuthState(AuthState.Awaiting)

                getAccessToken(
                    notionClientId = { authCredentials.clientId },
                    notionClientSecret = { authCredentials.clientSecret },
                    openUri = uriHandler::openUri,
                )

                onAuthState(AuthState.Authenticated)
            }
        },
        name = "login",
    )

    Subcommand(
        actionDescription = "Run notion auth logout to remove authentication with your Notion account.",
        onExecute = { tokenQueries.deleteAll() },
        name = "logout",
    )
}

internal data class AuthCredentials(
    val clientId: String,
    val clientSecret: String,
)

internal sealed interface AuthState : NotionState {
    data object Awaiting : AuthState
    data object Authenticated : AuthState
}
