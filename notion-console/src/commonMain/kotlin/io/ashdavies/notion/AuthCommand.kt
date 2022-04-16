package io.ashdavies.notion

import io.ashdavies.playground.AccessToken
import io.ashdavies.playground.OAuthProvider
import io.ashdavies.playground.Token
import io.ashdavies.playground.TokenQueries
import io.ashdavies.playground.beginAuthFlow
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

private const val AUTH_ACTION_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGIN_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGOUT_DESCRIPTION = "Run notion auth logout to remove authentication with your Notion account."

@ExperimentalCli
internal class AuthCommand(queries: TokenQueries) : Subcommand(
    actionDescription = AUTH_ACTION_DESCRIPTION,
    name = "auth",
) {
    init {
        val login = AuthLoginCommand(queries)
        val logout = AuthLogoutCommand(queries)

        subcommands(login, logout/*, refresh, status*/)
    }

    override fun execute() = Unit
}

@ExperimentalCli
private class AuthLoginCommand(private val queries: TokenQueries) : Subcommand(
    actionDescription = AUTH_LOGIN_DESCRIPTION,
    name = "login",
) {
    override fun execute() = runBlocking {
        val queryToken: Token? = queries
            .select()
            .executeAsOneOrNull()

        if (queryToken != null) {
            println("Authenticated with session token")
            return@runBlocking
        }

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

private suspend fun awaitToken(token: OAuthProvider.Notion, value: AccessToken) {

}

@ExperimentalCli
private class AuthLogoutCommand(private val queries: TokenQueries) : Subcommand(
    actionDescription = AUTH_LOGOUT_DESCRIPTION,
    name = "logout",
) {

    override fun execute() = runBlocking {
        println("Removing authentication")
        queries.deleteAll()
    }
}
