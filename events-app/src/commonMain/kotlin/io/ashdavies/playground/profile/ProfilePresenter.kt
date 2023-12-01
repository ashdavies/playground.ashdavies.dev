package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.playground.Profile
import kotlinx.serialization.Serializable

@Serializable
internal object ProfileScreen : Screen {
    sealed interface Event : CircuitUiEvent {
        data object Login : Event
    }

    data class State(
        val profile: Profile? = null,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun ProfilePresenter(
    navigator: Navigator,
    repository: ProfileRepository = rememberProfileRepository(),
    uriHandler: UriHandler = LocalUriHandler.current,
): ProfileScreen.State {
    val profile: Profile? by repository
        .getProfile(generateRandomIfEmpty = true)
        .collectAsState(initial = null)

    return ProfileScreen.State(profile) {
        if (it is ProfileScreen.Event.Login) uriHandler.openUri("http://localhost:8080/callback")
    }
}
