package io.ashdavies.notion

import androidx.compose.runtime.Composable
import io.ashdavies.notion.AuthState.Authenticated
import io.ashdavies.notion.cli.Subcommand
import io.ashdavies.notion.compose.rememberTokenQueries
import io.ashdavies.playground.OAuthProvider
import io.ashdavies.playground.Token
import io.ashdavies.playground.TokenQueries
import io.ashdavies.playground.getAccessToken
import kotlinx.cli.ExperimentalCli

private const val AUTH_ACTION_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGIN_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGOUT_DESCRIPTION = "Run notion auth logout to remove authentication with your Notion account."

@Composable
@ExperimentalCli
internal fun AuthCommand(
    queries: TokenQueries = rememberTokenQueries(),
    onAuthState: (AuthState) -> Unit = { },
) = Subcommand(name = "auth", AUTH_ACTION_DESCRIPTION) {
    Subcommand(
        actionDescription = AUTH_LOGIN_DESCRIPTION,
        onExecute = {
            when (queries.select().executeAsOneOrNull()) {
                null -> queries.insert(authenticate())
                else -> onAuthState(Authenticated)
            }
        },
        name = "login"
    )

    Subcommand(
        actionDescription = AUTH_LOGOUT_DESCRIPTION,
        onExecute = { queries.deleteAll() },
        name = "logout"
    )
}

internal sealed interface AuthState {
    object Authenticated : AuthState
}

private suspend fun authenticate(): Token {
    val userPromptUri = "http://localhost:8080/callback"
    println("Navigate to $userPromptUri to continue")

    val authToken = getAccessToken(OAuthProvider.Notion)
    println("Authentication complete")

    return Token(
        accessToken = authToken.accessToken,
        workspaceIcon = "",
        workspaceName = "",
        workspaceId = "",
        botId = "",
    )
}

