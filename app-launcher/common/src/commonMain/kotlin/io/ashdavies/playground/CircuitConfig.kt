package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitConfig
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Presenter
import com.slack.circuit.Ui

public fun CircuitConfig(
    componentContext: ComponentContext,
): CircuitConfig = CircuitConfig.Builder()
    .addPresenterFactory(LauncherPresenterFactory())
    .addUiFactory(LauncherUiFactory(componentContext))
    .build()

internal inline fun <UiState : CircuitUiState> Presenter(
    crossinline block: @Composable () -> UiState
): Presenter<UiState> = object : Presenter<UiState> {
    @Composable
    override fun present(): UiState = block()
}

internal inline fun <UiState : CircuitUiState> Ui(
    crossinline body: @Composable (state: UiState) -> Unit
): Ui<UiState> = object : Ui<UiState> {
    @Composable
    override fun Content(state: UiState) = body(state)
}
