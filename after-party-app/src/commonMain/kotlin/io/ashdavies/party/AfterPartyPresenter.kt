package io.ashdavies.party

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.Navigator

@Composable
internal fun AfterPartyPresenter(navigator: Navigator): AfterPartyScreen.State {
    return AfterPartyScreen.State.Initial
}
