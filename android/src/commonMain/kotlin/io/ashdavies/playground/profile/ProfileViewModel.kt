package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.OAuthQueries
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.kotlin.mapToOneOrNull
import io.ashdavies.playground.platform.PlatformCredentials
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import io.ktor.http.URLBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

internal class ProfileViewModel(
    private val profileService: ProfileService,
    private val oAuthQueries: OAuthQueries,
) : ViewModel() {

    private val _viewState = Channel<ProfileViewState>(CONFLATED)
    val viewState: StateFlow<ProfileViewState> = oAuthQueries.selectAll()
        .mapToOneOrNull { if (it == null) LoggedOut else LoggedIn(it) }
        .let { merge(_viewState.receiveAsFlow(), it) }
        .onEach { println("OnState $it") }
        .stateIn(viewModelScope, Eagerly, LoggedOut)

    fun onLogin() {
        println("OnLoginClicked")
        val uriString = URLBuilder("https://accounts.google.com/o/oauth2/v2/auth").apply {
            parameters.append("client_id", PlatformCredentials.serverClientId)
            parameters.append("redirect_uri", "http://localhost")
            parameters.append("response_type", "code")
            parameters.append("scope", "email")
        }.buildString()

        _viewState.trySend(ProfileViewState.LogIn(uriString))
    }
}

private fun LoggedIn(value: Oauth) = LoggedIn(
    picture = value.photoUrl,
    name = value.fullName,
    email = value.email,
)

@Composable
internal fun rememberProfileViewModel(
    profileService: ProfileService = rememberProfileService(),
    oAuthQueries: OAuthQueries = LocalPlaygroundDatabase.current.oAuthQueries,
): ProfileViewModel = remember(profileService, oAuthQueries) {
    ProfileViewModel(profileService, oAuthQueries)
}
