package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.network.Envelope
import io.ashdavies.playground.network.LocalHttpClient
import io.ashdavies.playground.network.Service
import io.ashdavies.playground.network.getting
import io.ashdavies.playground.network.posting
import io.ashdavies.playground.platform.PlatformCredentials
import io.ktor.client.HttpClient
import io.ktor.http.content.OutgoingContent
import kotlinx.serialization.Serializable

private const val IDENTITY_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts"
private const val RANDOM_USER = "https://randomuser.me/api/"

internal interface ProfileService : Service {
    val lookup: Service.Operator<OutgoingContent.NoContent, Envelope<RandomUser>>
    val signInWithIdp: Service.Operator<SignInWithIdp.Request, Oauth>
}

@Composable
internal fun rememberProfileService(
    httpClient: HttpClient = LocalHttpClient.current,
    apiKey: String = PlatformCredentials.webApiKey,
    endpoint: String = IDENTITY_ENDPOINT,
): ProfileService = remember(endpoint, apiKey) {
    object : ProfileService, Service by Service(httpClient) {
        override val lookup by getting<OutgoingContent.NoContent, Envelope<RandomUser>> { RANDOM_USER }
        override val signInWithIdp by posting<SignInWithIdp.Request, Oauth> { "$endpoint/$it?key=$apiKey" }
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
