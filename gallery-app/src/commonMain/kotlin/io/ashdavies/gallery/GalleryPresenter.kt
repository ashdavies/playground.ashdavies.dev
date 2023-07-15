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
        data class Result(val value: Uri) : Event
        data class Toggle(val index: Int) : Event
        object Capture : Event
        object Delete : Event
        object Pop : Event
    }

    internal sealed interface State : CircuitUiState {
        data class Capture(val eventSink: (Event) -> Unit) : State
        data class Empty(val eventSink: (Event) -> Unit) : State

        data class Success(val itemList: List<Item>, val eventSink: (Event) -> Unit) : State {
            data class Item(val value: Uri, val selected: Boolean)
        }

        object Loading : State
    }
}

public fun GalleryPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is GalleryScreen -> presenterOf { GalleryPresenter(rememberStorageProvider(), navigator) }
        else -> null
    }
}

public fun GalleryUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is GalleryScreen -> ui<GalleryScreen.State> { state, modifier -> GalleryScreen(state, modifier) }
        else -> null
    }
}

@Composable
internal fun GalleryPresenter(provider: StorageProvider, navigator: Navigator): GalleryScreen.State {
    val itemList by provider.images.collectAsState(emptyList())
    var selected by remember { mutableStateOf(mapOf<Int, Boolean>()) }
    var takePhoto by remember { mutableStateOf(false) }

    val eventSink: (GalleryScreen.Event) -> Unit = {
        when (it) {
            is GalleryScreen.Event.Capture -> takePhoto = true
            is GalleryScreen.Event.Delete -> selected.forEach { (index, value) ->
                if (value) provider.delete(itemList[index])
            }

            is GalleryScreen.Event.Pop -> navigator.pop()
            is GalleryScreen.Event.Result -> takePhoto = false
            is GalleryScreen.Event.Toggle -> selected += (it.index to !(selected[it.index] ?: false))
        }
    }

    return when (takePhoto) {
        false -> GalleryScreen.State.Success(
            itemList = itemList.mapIndexed { index, item ->
                GalleryScreen.State.Success.Item(
                    value = item,
                    selected = selected[index] ?: false,
                )
            },
            eventSink = eventSink,
        )

        true -> GalleryScreen.State.Capture(eventSink)
    }
}
