package io.ashdavies.notion

import androidx.compose.runtime.Composable
import io.ashdavies.playground.Token
import io.ashdavies.playground.TokenQueries
import kotlinx.cli.ExperimentalCli

@Composable
@ExperimentalCli
@NotionScopeMarker
internal fun AuthCommand(
    tokenQueries: TokenQueries = rememberTokenQueries(),
    onAuthState: (AuthState) -> Unit = { },
) = Subcommand(
    actionDescription = "Run notion auth login to authenticate with your Notion account.",
    name = "auth"
) {
    Subcommand(
        actionDescription = "Run notion auth login to authenticate with your Notion account.",
        onExecute = {
            val token = tokenQueries.select().executeAsOneOrNull()
            if (token != null) onAuthState(AuthState.Authenticated(token))
            else {
                onAuthState(AuthState.Awaiting("http://localhost:8080/callback"))
                val result = Token(getAccessToken(OAuthProvider.Notion))
                onAuthState(AuthState.Authenticated(result))
                tokenQueries.insert(result)
            }
        },
        name = "login"
    )

    Subcommand(
        actionDescription = "Run notion auth logout to remove authentication with your Notion account.",
        onExecute = { tokenQueries.deleteAll() },
        name = "logout"
    )
}

internal sealed interface AuthState : NotionState {
    data class Awaiting(val userPromptUri: String) : AuthState
    data class Authenticated(val token: Token) : AuthState
}

private fun Token(value: AccessToken) = Token(
    workspaceIcon = value.firstOrThrow("workspaceIcon"),
    workspaceName = value.firstOrThrow("workspaceName"),
    workspaceId = value.firstOrThrow("workspaceId"),
    botId = value.firstOrThrow("botId"),
    accessToken = value.accessToken,
)
