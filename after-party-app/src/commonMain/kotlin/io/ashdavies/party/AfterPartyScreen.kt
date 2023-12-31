package io.ashdavies.party

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

@Parcelize
public object AfterPartyScreen : Parcelable, Screen {
    internal sealed interface Event : CircuitUiEvent
    internal sealed interface State : CircuitUiState {
        data object Initial : State
    }
}

@Composable
internal fun AfterPartyScreen(
    state: AfterPartyScreen.State,
    modifier: Modifier = Modifier,
) {
    Text("After Party")
}
