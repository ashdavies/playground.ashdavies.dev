package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import io.ashdavies.playground.TokenQueries
import kotlinx.cli.ExperimentalCli

@Composable
@ExperimentalCli
@NotionScopeMarker
internal fun AuthCommand(
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
                getAccessToken(uriHandler::openUri)
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

internal sealed interface AuthState : NotionState {
    object Awaiting : AuthState
    object Authenticated : AuthState
}
