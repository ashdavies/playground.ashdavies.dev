package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.CircuitUiEvent
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Screen
import io.ashdavies.playground.Profile

@Parcelize
internal object ProfileScreen : Screen {
    sealed interface Event : CircuitUiEvent {
        data class BottomNav(val screen: Screen) : Event
        object Login : Event
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

    return ProfileScreen.State(profile) { event ->
        when (event) {
            ProfileScreen.Event.Login -> uriHandler.openUri("http://localhost:8080/callback")
            is ProfileScreen.Event.BottomNav -> navigator.resetRoot(event.screen)
        }
    }
}
