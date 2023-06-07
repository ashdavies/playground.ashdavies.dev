package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.append
import io.ktor.http.encodedPath
import io.ktor.http.formUrlEncode
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.awt.Desktop
import java.net.URI

private val applicationHttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    install(Logging) {
        level = LogLevel.ALL
    }
}

public val notionHttpClient: HttpClient = applicationHttpClient.config {
    install(Auth) {
        bearer {
            loadTokens {
                val deferredAuthorizationCode = CompletableDeferred<String>()
                val applicationEngine = embeddedServer(CIO, port = 8080) {
                    routing {
                        get("/callback") {
                            val authorizationCode = requireNotNull(call.parameters["code"])
                            call.respond(HttpStatusCode.NoContent)

                            deferredAuthorizationCode.complete(authorizationCode)
                        }
                    }
                }.start()

                val authorizationUrlQuery = parameters {
                    append("client_id", System.getenv("NOTION_CLIENT_ID"))
                    append("redirect_uri", "http://localhost:8080/callback")
                    append("response_type", "code")
                    append("owner", "user")
                }.formUrlEncode()

                val authorizationBaseUrl = "https://api.notion.com/v1/oauth/authorize"
                val authorizationUrl = "$authorizationBaseUrl?$authorizationUrlQuery"
                Desktop.getDesktop().browse(URI(authorizationUrl))

                val authorizationCode = deferredAuthorizationCode.await()
                applicationEngine.stop()

                // Should be application encoded json POST body
                val tokenUrlString = "https://api.notion.com/v1/oauth/token"
                val tokenResponse = applicationHttpClient.post(tokenUrlString) {
                    basicAuth(
                        username = System.getenv("NOTION_CLIENT_ID"),
                        password = System.getenv("NOTION_CLIENT_SECRET"),
                    )

                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                        append("Notion-Version", "2022-06-28")
                    }

                    val payload = mapOf(
                        "redirect_uri" to "http://localhost:8080/callback",
                        "grant_type" to "authorization_code",
                        "code" to authorizationCode,
                    )

                    setBody(payload)
                }

                if (tokenResponse.status != HttpStatusCode.OK) {
                    val error = tokenResponse.body<Notion.Error>()
                    println("Error: ${error.message}")
                    return@loadTokens null
                }

                val token = tokenResponse.body<Notion.Token>()
                BearerTokens(token.accessToken, String())
            }

            sendWithoutRequest { request ->
                request.url.host == "api.notion.com"
            }
        }
    }

    install(DefaultRequest) {
        headers {
            append("Notion-Version", "2022-06-28")
        }

        url {
            protocol = URLProtocol.HTTPS
            host = "api.notion.com"
            encodedPath = "/v1/"
        }
    }
}

public suspend fun getAccessToken(): String {
    val response = notionHttpClient.get("users/me")
    
    if (response.status != HttpStatusCode.OK) {
        val error = response.body<Notion.Error>()
        throw RuntimeException(error.message)
    }

    return response.request
        .headers[HttpHeaders.Authorization]!!
        .substringAfter(" ")
        .also { println(it) }
}

internal object Notion {

    @Serializable
    data class Bot(
        @SerialName("owner") val owner: Owner,
        @SerialName("workspace_name") val workspaceName: String,
    )

    @Serializable
    data class Error(
        @SerialName("object") val `object`: String,
        @SerialName("status") val status: Int,
        @SerialName("code") val code: String,
        @SerialName("message") val message: String,
    )

    @Serializable
    data class Person(
        @SerialName("email") val email: String,
    )

    @Serializable
    data class Token(
        @SerialName("access_token") val accessToken: String,
        @SerialName("token_type") val tokenType: String,
        @SerialName("bot_id") val botId: String,
        @SerialName("owner") val owner: Owner,
        @SerialName("workspace_icon") val workspaceIcon: String,
        @SerialName("workspace_id") val workspaceId: String,
        @SerialName("workspace_name") val workspaceName: String,
    ) {

        @Serializable
        data class Owner(
            @SerialName("type") val type: String,
            @SerialName("user") val user: User,
        )
    }

    @Serializable
    sealed class Owner {

        @Serializable
        @SerialName("bot")
        data class Bot(
            @SerialName("bot") val bot: Notion.Bot,
        ) : Owner()

        @Serializable
        @SerialName("person")
        data class Person(
            @SerialName("person") val person: Notion.Person,
        ) : Owner()

        @Serializable
        @SerialName("user")
        data class User(
            @SerialName("user") val user: Notion.User,
        ) : Owner()
    }

    @Serializable
    data class User(
        @SerialName("object") val `object`: String,
        @SerialName("id") val id: String,
        @SerialName("name") val name: String,
        @SerialName("avatar_url") val avatarUrl: String? = null,
        @SerialName("bot") val bot: Bot? = null,
    )
}
