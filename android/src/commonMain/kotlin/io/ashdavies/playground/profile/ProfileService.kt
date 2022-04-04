package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.network.LocalHttpClient
import io.ashdavies.playground.network.Service
import io.ashdavies.playground.network.ServiceOperator
import io.ashdavies.playground.network.serviceOperator
import io.ashdavies.playground.platform.PlatformCredentials
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

private const val IDENTITY_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts"

public interface ProfileService : Service {
    val signInWithIdp: ServiceOperator<SignInWithIdp.Request, Oauth>
}

@Composable
public fun rememberProfileService(
    httpClient: HttpClient = LocalHttpClient.current,
    apiKey: String = PlatformCredentials.webApiKey,
    endpoint: String = IDENTITY_ENDPOINT,
): ProfileService = remember(endpoint, apiKey) {
    object : ProfileService {
        override val signInWithIdp by serviceOperator<SignInWithIdp.Request, Oauth>(httpClient) {
            "$endpoint/$it?key=$apiKey"
        }
    }
}

public sealed class SignInWithIdp {
    @Serializable data class Request(
        val requestUri: String,
        val postBody: String,
        val returnSecureToken: Boolean = true,
        val returnIdpCredential: Boolean = false,
    ) : SignInWithIdp()
}
