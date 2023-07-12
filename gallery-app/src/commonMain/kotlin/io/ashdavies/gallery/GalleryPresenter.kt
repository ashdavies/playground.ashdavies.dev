package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
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
        object Capture : Event
        object Pop : Event
    }

    internal sealed interface State : CircuitUiState {
        data class Success(
            val itemList: List<Uri>,
            val eventSink: (Event) -> Unit,
        ) : State

        data class Empty(
            val eventSink: (Event) -> Unit,
        ) : State

        object Loading : State
    }
}

@Parcelize
internal object CameraScreen : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        data class Result(val value: Uri) : Event
        object Pop : Event
    }

    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

public fun GalleryPresenterFactory(): Presenter.Factory {
    val storage = GalleryStorage()

    return Presenter.Factory { screen, navigator, _ ->
        when (screen) {
            is GalleryScreen -> presenterOf { GalleryPresenter(storage, navigator) }
            is CameraScreen -> presenterOf { CameraPresenter(storage, navigator) }
            else -> null
        }
    }
}

public fun GalleryUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is GalleryScreen -> ui<GalleryScreen.State> { state, modifier -> GalleryScreen(state, modifier) }
        is CameraScreen -> ui<CameraScreen.State> { state, modifier -> CameraScreen(state, modifier) }
        else -> null
    }
}

@Composable
internal fun GalleryPresenter(
    storage: GalleryStorage,
    navigator: Navigator,
): GalleryScreen.State {
    val itemList by storage.items().collectAsState(emptyList())

    return GalleryScreen.State.Success(itemList) {
        when (it) {
            is GalleryScreen.Event.Capture -> navigator.goTo(CameraScreen)
            is GalleryScreen.Event.Pop -> navigator.pop()
        }
    }
}

@Composable
internal fun CameraPresenter(
    storage: GalleryStorage,
    navigator: Navigator,
) = CameraScreen.State {
    when (it) {
        is CameraScreen.Event.Result -> storage.store(it.value)
        is CameraScreen.Event.Pop -> navigator.pop()
    }
}
