package io.ashdavies.party

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.activity.ActivityScreen
import io.ashdavies.identity.IdentityState

@Composable
internal fun AfterPartyPresenter(navigator: Navigator): AfterPartyScreen.State {
    var screen by rememberRetained { mutableStateOf<Screen>(ActivityScreen) }

    return AfterPartyScreen.State(
        identityState = IdentityState.Unsupported,
        screen = screen,
    ) { event ->
        when (event) {
            is AfterPartyScreen.Event.ChildNav -> navigator.onNavEvent(event.navEvent)
            is AfterPartyScreen.Event.BottomNav -> screen = event.screen
        }
    }
}
