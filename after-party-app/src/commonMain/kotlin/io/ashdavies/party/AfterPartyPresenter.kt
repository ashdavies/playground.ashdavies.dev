package io.ashdavies.party

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.identity.IdentityState
import io.ashdavies.playground.activity.ActivityScreen

@Composable
internal fun AfterPartyPresenter(navigator: Navigator): AfterPartyScreen.State {
    var screen by remember { mutableStateOf<Screen>(ActivityScreen) }

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
