package dev.ashdavies.playground.ui.scaffold.adaptive

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.serialization.CircuitSerializable
import dev.zacsweers.metro.AppScope
import kotlinx.serialization.Serializable

@Serializable
@CircuitSerializable(AppScope::class)
public data class ListDetailScaffoldScreen(val initialScreen: Screen) : Screen {

    public data class State(val initialScreen: Screen) : CircuitUiState

    public interface ExtraPaneScreen {
        public val extraScreen: Screen
    }
}
