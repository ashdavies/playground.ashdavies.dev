package io.ashdavies.notion

import kotlinx.cli.ExperimentalCli
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.oauth.OAuthCodeAndState
import org.jraf.klibnotion.model.oauth.OAuthCredentials
import org.jraf.klibnotion.model.oauth.OAuthGetAccessTokenResult

private const val AUTH_ACTION_DESCRIPTION =
    "Run notion auth login to authenticate with your Notion account."

private const val AUTH_LOGIN_DESCRIPTION =
    "Run notion auth login to authenticate with your Notion account."

private const val AUTH_LOGOUT_DESCRIPTION =
    "Run notion auth logout to remove authentication with your Notion account."

private const val API_NOTION_V1 =
    "https://api.notion.com/v1"

private const val AUTH_SERVER_LOCALHOST = "localhost"
private const val AUTH_SERVER_CALLBACK = "callback"
private const val AUTH_SERVER_PORT = 8080

private val notionClientId: String
    get() = Environment["NOTION_CLIENT_ID"]

private val notionClientSecret: String
    get() = Environment["NOTION_CLIENT_SECRET"]

@ExperimentalCli
internal class AuthCommand(
    client: NotionClient.OAuth,
    queries: AuthResponseQueries,
) : CloseableSubcommand(
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

    override suspend fun run() = Unit
}

@ExperimentalCli
private class AuthLoginCommand(
    private val client: NotionClient.OAuth,
    private val queries: AuthResponseQueries,
) : CloseableSubcommand(
    actionDescription = AUTH_LOGIN_DESCRIPTION,
    name = "login",
) {
    override suspend fun run() {
        val queryToken: Authorisation? = queries
            .select()
            .executeAsOneOrNull()

        if (queryToken != null) {
            println("Authenticated with session token")
            return
        }

        val redirectUri: String = StringBuilder("$API_NOTION_V1/oauth/authorize?")
            .append("client_id=$notionClientId&")
            .append("redirect_uri=$notionClientSecret&")
            .append("response_type=code")
            .toString()

        val authCredentials = OAuthCredentials(
            clientSecret = notionClientSecret,
            clientId = notionClientId,
            redirectUri = redirectUri,
        )

        val uniqueState = randomUuid()
        val uriString = client.getUserPromptUri(
            oAuthCredentials = authCredentials,
            uniqueState = uniqueState,
        )

        val authServer = AuthServer(
            host = AUTH_SERVER_LOCALHOST,
            path = AUTH_SERVER_CALLBACK,
            port = AUTH_SERVER_PORT,
        )

        Browser.launch(uriString)

        val authResponse: OAuthCodeAndState? = client.extractCodeAndStateFromRedirectUri(
            redirectUri = authServer.awaitRedirectUri()
        )

        if (authResponse == null || authResponse.code != uniqueState) {
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
private class AuthLogoutCommand(
    private val queries: AuthResponseQueries,
) : CloseableSubcommand(
    actionDescription = AUTH_LOGOUT_DESCRIPTION,
    name = "logout",
) {

    override suspend fun run() {
        println("Removing authentication")
        queries.deleteAll()
    }
}
