package io.ashdavies.notion

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli

private const val AUTH_ACTION_DESCRIPTION =
    "Run notion auth login to authenticate with your Notion account."

private const val AUTH_LOGIN_DESCRIPTION =
    "Run notion auth login to authenticate with your Notion account."

private const val WITH_TOKEN_DESCRIPTION =
    "Authenticate with Notion by reading the token from standard input."

private const val AUTH_LOGOUT_DESCRIPTION =
    "Run notion auth logout to remove authentication with your Notion account."

private val notionClientId: String
    get() = Environment["NOTION_CLIENT_ID"]

private val notionClientSecret: String
    get() = Environment["NOTION_CLIENT_SECRET"]

/**
 * notion auth
 *  login
 *  logout
 *  refresh
 *  status
 */
@ExperimentalCli
internal class AuthCommand(
    client: NotionClient,
    queries: AuthResponseQueries,
    printer: Printer = Printer(),
) : CloseableSubcommand(
    actionDescription = AUTH_ACTION_DESCRIPTION,
    closeable = client,
    name = "auth",
) {
    init {
        val login = AuthLoginCommand(
            printer = printer,
            queries = queries,
            client = client,
        )

        val logout = AuthLogoutCommand(
            printer = printer,
            queries = queries,
        )

        subcommands(login, logout)
    }

    override suspend fun run() = Unit
}

@ExperimentalCli
private class AuthLoginCommand(
    private val client: NotionClient,
    private val queries: AuthResponseQueries,
    private val printer: Printer,
) : CloseableSubcommand(
    actionDescription = AUTH_LOGIN_DESCRIPTION,
    closeable = client,
    name = "login",
) {

    private val withToken: String? by option(
        description = WITH_TOKEN_DESCRIPTION,
        fullName = "with_token",
        type = ArgType.String,
    )

    override suspend fun run() {
        val withToken: String? = withToken
        if (withToken != null) {
            printer.println { "Authenticating with input token..." }
            queries.insert(accessToken = withToken)
        }

        val queryToken: Authorisation? = queries
            .select()
            .executeAsOneOrNull()

        if (queryToken == null) {
            printer.println { "Token history not found in local database" }
        }

        if (withToken != null && queryToken == null) {
            throw IllegalStateException("Failed to register input token")
        }

        if (withToken == null && queryToken != null) {
            printer.println { "Authenticated with session token" }
        }

        if (withToken == null && queryToken == null) {
            printer.println { "Authenticating with Notion, please continue in the browser..." }
            queries.insert(client.auth(notionClientId, notionClientSecret))
            // TODO Create user prompt and print interaction via client.auth
            printer.println { "Authentication complete" }
        }
    }
}

@ExperimentalCli
private class AuthLogoutCommand(
    private val queries: AuthResponseQueries,
    private val printer: Printer,
) : CloseableSubcommand(
    actionDescription = AUTH_LOGOUT_DESCRIPTION,
    closeable = Closeable { },
    name = "logout",
) {

    override suspend fun run() {
        queries.deleteAll()
        printer.println { "Authentication removed" }
    }
}
