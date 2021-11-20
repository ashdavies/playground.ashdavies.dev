package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val LOCALHOST = "localhost"
private const val CALLBACK = "callback"

private const val PORT = 8080

internal fun HttpNotionAuth(
    client: HttpClient,
    baseUrl: String,
) = NotionAuth { clientId, clientSecret ->
    val redirectUri = "http://$LOCALHOST:$PORT/callback"
    val uriString: String = StringBuilder("$baseUrl/oauth/authorize?")
        .append("client_id=$clientId&")
        .append("redirect_uri=$redirectUri&")
        .append("response_type=code")
        .toString()

    val config = AuthServerConfig(LOCALHOST, CALLBACK, PORT)
    val server = AuthServer(config)

    println("Open $uriString in the browser")
    //Browser.launch(uriString)

    val code: String = when (val result: AuthResult = server.await()) {
        is AuthResult.Error -> throw IllegalStateException(result.error)
        is AuthResult.Success -> result.code
    }

    val client: HttpClient = client.config(clientId, clientSecret)
    val result: AuthResponse = client.post("$baseUrl/oauth/token") {
        contentType(ContentType.Application.Json)
        body = TokenRequest(code, redirectUri)
    }

    client.close()
    server.close()
    result
}
