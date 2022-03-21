package io.ashdavies.playground.profile

import io.ashdavies.playground.Profile
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.network.invoke
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ProfileViewModel(private val profileService: ProfileService) : ViewModel() {

    val viewState = flow<Profile?> { profileService.lookup(Lookup.Request("")) }
        .map { if (it == null) LoggedOut else LoggedIn(it) }
        .stateIn(viewModelScope, Eagerly, LoggedOut)

    fun onLogin() {
        viewModelScope.launch {
            println("Navigate to `${profileService.createAuthUri()}`")
        }
    }
}
