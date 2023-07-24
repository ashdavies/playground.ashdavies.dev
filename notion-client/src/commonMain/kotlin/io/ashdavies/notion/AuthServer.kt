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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import java.io.File

private val APPLICATION_HTTP_CLIENT = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }

    install(Logging) {
        level = LogLevel.ALL
    }
}

public fun getNotionHttpClient(openUri: (String) -> Unit): HttpClient = APPLICATION_HTTP_CLIENT.config {
    install(Auth) {
        bearer {
            loadTokens {
                val accessTokenFile = File("tokens.db")
                if (accessTokenFile.exists()) {
                    return@loadTokens BearerTokens(
                        accessToken = accessTokenFile.readText(),
                        refreshToken = String(),
                    )
                }

                val coroutineScope = CoroutineScope(Dispatchers.Default)
                val deferredAuthorizationCode = coroutineScope.async {
                    awaitAuthorizationCode()
                }

                val redirectUrlString = getRedirectUrlString()
                val authorizationUrlQuery = parameters {
                    append("redirect_uri", redirectUrlString)
                    append("client_id", NotionClientId)
                    append("response_type", "code")
                    append("owner", "user")
                }.formUrlEncode()

                println("=== Preparing authorization query... ($authorizationUrlQuery)")
                val authorizationBaseUrl = "https://api.notion.com/v1/oauth/authorize"
                val authorizationUrl = "$authorizationBaseUrl?$authorizationUrlQuery"
                openUri(authorizationUrl)


                val tokenUrlString = "https://api.notion.com/v1/oauth/token"
                val tokenResponse = APPLICATION_HTTP_CLIENT.post(tokenUrlString) {
                    basicAuth(
                        username = NotionClientId,
                        password = NotionClientSecret,
                    )

                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                        append("Notion-Version", "2022-06-28")
                    }

                    val payload = mapOf(
                        "code" to deferredAuthorizationCode.await(),
                        "grant_type" to "authorization_code",
                        "redirect_uri" to redirectUrlString,
                    )

                    coroutineScope.cancel()
                    println("=== Payload = $payload")
                    setBody(payload)
                }

                if (tokenResponse.status != HttpStatusCode.OK) {
                    val error = tokenResponse.body<Notion.Object.Error>()
                    throw IllegalStateException(error.message)
                }

                val accessToken = tokenResponse
                    .body<Notion.Token>()
                    .accessToken

                accessTokenFile.writeText(accessToken)
                BearerTokens(accessToken, String())
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

internal expect suspend fun awaitAuthorizationCode(): String

internal expect fun getRedirectUrlString(): String

@Deprecated("Do not call this method directly")
public suspend fun getAccessToken(openUri: (String) -> Unit): String {
    val notionHttpClient = getNotionHttpClient(openUri)
    val response = notionHttpClient.get("users/me")

    if (response.status != HttpStatusCode.OK) {
        val error = response.body<Notion.Object.Error>()
        throw RuntimeException(error.message)
    }

    return response.request
        .headers[HttpHeaders.Authorization]!!
        .substringAfter(" ")
}

public object Notion {

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    public sealed class Envelope {

        @Serializable
        @SerialName("person")
        public data class Person(
            @SerialName("person") val person: Object.Person,
        ) : Envelope()

        @Serializable
        @SerialName("user")
        public data class User(
            @SerialName("user") val user: Object.User,
        ) : Envelope()
    }

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("object")
    public sealed class Object {

        @Serializable
        public data class Bot(
            @SerialName("owner") val owner: Object,
            @SerialName("workspace_name") val workspaceName: String,
        )

        @Serializable
        @SerialName("database")
        public data class Database(
            @SerialName("id") val id: String,
        ) : Object()

        @Serializable
        @SerialName("error")
        public data class Error(
            @SerialName("status") val status: Int,
            @SerialName("code") val code: String,
            @SerialName("message") val message: String,
        ) : Object()

        @Serializable
        @SerialName("page")
        public data class Page(
            @SerialName("id") val id: String,
            @SerialName("properties") val properties: Map<String, Property>,
            @SerialName("url") val url: String,
        ) : Object()

        @Serializable
        @SerialName("person")
        public data class Person(
            @SerialName("email") val email: String,
        ) : Object()

        @Serializable
        @SerialName("search")
        public data class Search(
            @SerialName("results") val results: List<Object>,
            @SerialName("next_cursor") val nextCursor: String?,
            @SerialName("has_more") val hasMore: Boolean,
        ) : Object()

        @Serializable
        @SerialName("user")
        public data class User(
            @SerialName("id") val id: String,
            @SerialName("name") val name: String,
            @SerialName("avatar_url") val avatarUrl: String? = null,
            @SerialName("bot") val bot: Bot? = null,
        ) : Object()
    }

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("type")
    public sealed class Property {

        @Serializable
        @SerialName("date")
        public data class Date(
            @SerialName("date") val date: String?,
        ) : Property()

        @Serializable
        @SerialName("formula")
        public data class Formula(
            @SerialName("formula") val formula: Property,
        ) : Property()

        @Serializable
        @SerialName("last_edited_time")
        public data class LastEditedTime(
            @SerialName("last_edited_time") val lastEditedTime: String,
        ) : Property()

        @Serializable
        @SerialName("multi_select")
        public data class MultiSelect(
            @SerialName("multi_select") val multiSelect: List<String>,
        ) : Property()

        @Serializable
        @SerialName("number")
        public data class Number(
            @SerialName("number") val number: Double?,
        ) : Property()

        @Serializable
        @SerialName("status")
        public data class Status(
            @SerialName("status") val status: Label,
        ) : Property() {

            @Serializable
            public data class Label(
                @SerialName("name") val label: String,
            ) : Property()
        }

        @Serializable
        @SerialName("text")
        public data class Text(
            @SerialName("plain_text") val plainText: String,
        ) : Property()

        @Serializable
        @SerialName("title")
        public data class Title(
            @SerialName("title") val title: List<Text>,
        ) : Property()

        @Serializable
        @SerialName("url")
        public data class Url(
            @SerialName("url") val url: String?,
        ) : Property()
    }

    @Serializable
    public data class Token(
        @SerialName("access_token") val accessToken: String,
        @SerialName("token_type") val tokenType: String,
        @SerialName("bot_id") val botId: String,
        @SerialName("workspace_icon") val workspaceIcon: String,
        @SerialName("workspace_id") val workspaceId: String,
        @SerialName("workspace_name") val workspaceName: String,
        @SerialName("owner") val owner: Envelope,
    )
}
