package io.ashdavies.notion

import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.oauth.OAuthCodeAndState
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.oauth.OAuthGetAccessTokenResult
import kotlin.time.ExperimentalTime

private const val AUTH_ACTION_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGIN_DESCRIPTION = "Run notion auth login to authenticate with your Notion account."
private const val AUTH_LOGOUT_DESCRIPTION = "Run notion auth logout to remove authentication with your Notion account."

private val notionClientId: String
    get() = System.getenv("NOTION_CLIENT_ID")

private val notionClientSecret: String
    get() = System.getenv("NOTION_CLIENT_SECRET")

@ExperimentalCli
internal class AuthCommand(client: NotionClient.OAuth, queries: AuthResponseQueries) : Subcommand(
    actionDescription = AUTH_ACTION_DESCRIPTION,
    name = "auth",
) {
    init {
        val login = AuthLoginCommand(
            queries = queries,
            client = client,
        )

        val logout = AuthLogoutCommand(
            queries = queries,
        )

        subcommands(login, logout/*, refresh, status*/)
    }

    override fun execute() = Unit
}

@ExperimentalCli
@OptIn(ExperimentalTime::class)
private class AuthLoginCommand(
    private val client: NotionClient.OAuth,
    private val queries: AuthResponseQueries,
) : Subcommand(
    actionDescription = AUTH_LOGIN_DESCRIPTION,
    name = "login",
) {
    override fun execute() = runBlocking {
        val queryToken: Authorisation? = queries
            .select()
            .executeAsOneOrNull()

        if (queryToken != null) {
            println("Authenticated with session token")
            return@runBlocking
        }

        val authServer = AuthServer()
        val authCredentials = OAuthCredentials(
            redirectUri = authServer.getRedirectUri(),
            clientSecret = notionClientSecret,
            clientId = notionClientId,
        )

        val uniqueState = randomUuid()
        val userPromptUri = client.getUserPromptUri(
            oAuthCredentials = authCredentials,
            uniqueState = uniqueState,
        )

        if (!Browser.launch(userPromptUri)) {
            println("Navigate to $userPromptUri to continue")
        }

        val authResponse: OAuthCodeAndState? = client.extractCodeAndStateFromRedirectUri(
            redirectUri = authServer.awaitRedirectUri()
        )

        if (authResponse == null || authResponse.state != uniqueState) {
            throw IllegalStateException("Invalid auth response")
        }

        val authResult: OAuthGetAccessTokenResult = client.getAccessToken(
            oAuthCredentials = authCredentials,
            code = authResponse.code,
        )

        queries.insert(
            accessToken = authResult.accessToken,
            workspaceId = authResult.workspaceId,
            workspaceName = authResult.workspaceName,
            workspaceIcon = authResult.workspaceIcon,
            botId = authResult.botId,
        )

        println("Authentication complete")
    }
}

@ExperimentalCli
private class AuthLogoutCommand(private val queries: AuthResponseQueries) : Subcommand(
    actionDescription = AUTH_LOGOUT_DESCRIPTION,
    name = "logout",
) {

    override fun execute() = runBlocking {
        println("Removing authentication")
        queries.deleteAll()
    }
}
