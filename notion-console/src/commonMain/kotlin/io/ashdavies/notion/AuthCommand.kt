package io.ashdavies.notion

import androidx.compose.runtime.Composable
import io.ashdavies.playground.AccessToken
import io.ashdavies.playground.OAuthProvider
import io.ashdavies.playground.Token
import io.ashdavies.playground.TokenQueries
import io.ashdavies.playground.beginAuthFlow
import kotlinx.cli.ExperimentalCli
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val AUTH_LOGIN_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGOUT_DESCRIPTION = "Run notion auth logout to remove authentication with your Notion account."

@Composable
@ExperimentalCli
internal fun AuthCommand(queries: TokenQueries = rememberTokenQueries()) {
    ArgParser("auth", "Run notion auth login to authenticate with your Notion account.") {
        authLoginCommand(queries)
        authLogoutCommand(queries)
    }
}

@Composable
@ExperimentalCli
private fun authLoginCommand(queries: TokenQueries) = rememberSubcommand("login", AUTH_LOGIN_DESCRIPTION) {
    val queryToken: Token? = queries.select().executeAsOneOrNull()
    if (queryToken != null) println("Authenticated with session token")
    else {
        queries.insert(authenticate())
        println("Authentication complete")
    }
}

private suspend fun CoroutineScope.authenticate(): Token {
    val deferredAuthResult = CompletableDeferred<AccessToken>()
    val userPromptUri = "http://localhost:8080/callback"

    beginAuthFlow(OAuthProvider.Notion)
        .onEach { deferredAuthResult.complete(it) }
        .launchIn(this)

    // Await server startup
    delay(500)

    if (!Browser.launch(userPromptUri)) {
        println("Navigate to $userPromptUri to continue")
    }

    val authResult = deferredAuthResult.await()

    return Token(
        accessToken = authResult.accessToken,
        workspaceIcon = "",
        workspaceName = "",
        workspaceId = "",
        botId = "",
    )
}

@Composable
@ExperimentalCli
private fun authLogoutCommand(queries: TokenQueries) = rememberSubcommand("logout", AUTH_LOGOUT_DESCRIPTION) {
    println("Removing authentication")
    queries.deleteAll()
}
