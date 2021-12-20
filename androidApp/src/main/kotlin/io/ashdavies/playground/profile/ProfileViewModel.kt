package io.ashdavies.playground.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ashdavies.playground.database.Profile
import io.ashdavies.playground.profile.ProfileViewState.LoggedIn
import io.ashdavies.playground.profile.ProfileViewState.LoggedOut
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ProfileViewModel(private val profileService: ProfileService) : ViewModel() {

    val viewState = flow<Profile?> { profileService.getProfile() }
        .map { if (it == null) LoggedOut else LoggedIn(it) }
        .stateIn(viewModelScope, Eagerly, LoggedOut)

    fun onLogin() {
        viewModelScope.launch {
            profileService.authenticate()
        }
    }
}
