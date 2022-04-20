package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.OAuthQueries
import io.ashdavies.playground.Oauth
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.kotlin.mapToOneOrNull
import io.ashdavies.playground.invoke
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            _viewState.send(ProfileViewState.LogIn("http://localhost:8080/callback"))

            // val accessToken = getAccessToken(OAuthProvider.Google)
            val randomUser = profileService.lookup().results.first()

            _viewState.send(LoggedIn(randomUser))
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
    profileService: ProfileService = rememberProfileService(),
    oAuthQueries: OAuthQueries = LocalPlaygroundDatabase.current.oAuthQueries,
): ProfileViewModel = viewModel { ProfileViewModel(profileService, oAuthQueries) }
