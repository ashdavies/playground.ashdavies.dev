package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

@Parcelize
public object GalleryScreen : Parcelable, Screen {

    internal sealed interface Event : CircuitUiEvent {
        object Pop : Event
    }

    internal data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

public class GalleryPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is GalleryScreen -> presenterOf { GalleryPresenter(navigator) }
        else -> null
    }
}

public class GalleryUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is GalleryScreen -> ui<GalleryScreen.State> { state, modifier ->
            GalleryScreen(state, modifier)
        }

        else -> null
    }
}

@Composable
internal fun GalleryPresenter(
    navigator: Navigator,
) = GalleryScreen.State {
    when (it) {
        is GalleryScreen.Event.Pop -> navigator.pop()
    }
}
