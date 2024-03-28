package io.ashdavies.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.retained.produceRetainedState

@Composable
internal fun ProfilePresenter(
    repository: ProfileRepository = rememberProfileRepository(),
    uriHandler: UriHandler = LocalUriHandler.current,
): ProfileScreen.State {
    val profile by produceRetainedState<Profile?>(null) {
        repository.getProfile(generateRandomIfEmpty = true).collect { value = it }
    }

    return ProfileScreen.State(profile) {
        if (it is ProfileScreen.Event.Login) uriHandler.openUri("http://localhost:8080/callback")
    }
}
