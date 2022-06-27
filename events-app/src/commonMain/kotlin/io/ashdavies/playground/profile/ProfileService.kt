package io.ashdavies.playground.profile

import io.ashdavies.http.path
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.ObsoletePlaygroundApi
import io.ashdavies.playground.PlaygroundService
import io.ashdavies.playground.requesting
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent.NoContent
import kotlinx.serialization.Serializable

private const val IDENTITY_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts"
private const val RANDOM_USER = "https://randomuser.me/api/"

@ObsoletePlaygroundApi
internal class ProfileService(client: HttpClient, key: String) : PlaygroundService(client) {
    val lookup: Operator<NoContent, Envelope<RandomUser>> by requesting { path(RANDOM_USER) }
    val signInWithIdp: Operator<SignInWithIdp.Request, Oauth> by requesting {
        path("$IDENTITY_ENDPOINT/$it?key=$key")
        method = HttpMethod.Post
        setBody(it)
    }
}

internal sealed class SignInWithIdp {
    @Serializable data class Request(
        val requestUri: String,
        val postBody: String,
        val returnSecureToken: Boolean = true,
        val returnIdpCredential: Boolean = false,
    ) : SignInWithIdp()
}

@Serializable
internal data class Envelope<T>(val results: List<T>)
