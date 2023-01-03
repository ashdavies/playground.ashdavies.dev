package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Screen
import com.slack.circuit.ScreenUi
import com.slack.circuit.Ui

public class LauncherUiFactory(private val componentContext: ComponentContext) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): ScreenUi? {
        return when (screen) {
            is LauncherScreen -> ScreenUi(Ui<LauncherState> {
                LauncherScreen(componentContext, it)
            })

            else -> null
        }
    }
}

private inline fun <UiState : CircuitUiState> Ui(
    crossinline body: @Composable (state: UiState) -> Unit
): Ui<UiState> = object : Ui<UiState> {
    @Composable
    override fun Content(state: UiState) = body(state)
}
