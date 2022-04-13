package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.OAuthProvider
import io.ashdavies.playground.OAuthQueries
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.beginAuthFlow
import io.ashdavies.playground.kotlin.mapToOneOrNull
import io.ashdavies.playground.network.invoke
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
        .stateIn(viewModelScope, Eagerly, LoggedOut)

    fun onLogin() {
        beginAuthFlow(OAuthProvider.Google)
            .map { profileService.lookup().results }
            .onEach { _viewState.send(LoggedIn(it.first())) }
            .launchIn(viewModelScope)

        _viewState.trySend(ProfileViewState.LogIn("http://localhost:8080/callback"))
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
    profileService: ProfileService = rememberProfileService(),
    oAuthQueries: OAuthQueries = LocalPlaygroundDatabase.current.oAuthQueries,
): ProfileViewModel = viewModel { ProfileViewModel(profileService, oAuthQueries) }
