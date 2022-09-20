package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.filterIsSuccess
import io.ashdavies.http.requesting
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.OAuthQueries
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.kotlin.mapToOneOrNull
import io.ashdavies.playground.profile.ProfileViewState.LogIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import io.ktor.client.HttpClient
import io.ktor.http.takeFrom
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

private const val IDENTITY_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts"
private const val RANDOM_USER = "https://randomuser.me/api/"

internal class ProfileViewModel(
    private val client: HttpClient,
    queries: OAuthQueries,
) : ViewModel() {

    private val _viewState = Channel<ProfileViewState>(CONFLATED)
    val viewState: StateFlow<ProfileViewState> = queries.selectAll()
        .mapToOneOrNull { if (it == null) LoggedOut else LoggedIn(it) }
        .let { merge(_viewState.receiveAsFlow(), it) }
        .stateIn(viewModelScope, Eagerly, LoggedOut)

    fun onLogin() {
        viewModelScope.launch {
            client.requesting<Envelope<RandomUser>> { url.takeFrom(RANDOM_USER) }
                .filterIsSuccess<Envelope<RandomUser>, ProfileViewState> { LoggedIn(it.results.first()) }
                .onStart { emit(LogIn("http://localhost:8080/callback")) }
                .collect(_viewState::send)
        }
    }
}

private fun LoggedIn(value: RandomUser) = LoggedIn(
    name = "${value.name.first} ${value.name.last}",
    picture = value.picture.large,
    email = value.email,
)

private fun LoggedIn(value: Oauth) = LoggedIn(
    picture = value.photoUrl,
    name = value.fullName,
    email = value.email,
)

@Composable
internal fun rememberProfileViewModel(
    client: HttpClient = LocalHttpClient.current,
    queries: OAuthQueries = LocalPlaygroundDatabase.current.oAuthQueries,
): ProfileViewModel = viewModel { ProfileViewModel(client, queries) }

@Serializable
private data class Envelope<T>(val results: List<T>)

@Serializable
private data class SignInWithIdpRequest(
    val returnIdpCredential: Boolean = false,
    val returnSecureToken: Boolean = true,
    val requestUri: String,
    val postBody: String,
)
