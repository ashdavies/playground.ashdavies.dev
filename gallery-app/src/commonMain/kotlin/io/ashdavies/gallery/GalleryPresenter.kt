package io.ashdavies.gallery

import androidx.compose.runtime.Composable
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
        data class Result(val value: File) : Event
        data class Toggle(val index: Int) : Event
        object Capture : Event
        object Delete : Event
    }

    internal sealed interface State : CircuitUiState {
        data class Capture(val eventSink: (Event) -> Unit) : State
        data class Empty(val eventSink: (Event) -> Unit) : State

        data class Success(val itemList: List<Item>, val eventSink: (Event) -> Unit) : State {
            constructor(itemList: List<File>, isSelected: (File) -> Boolean, eventSink: (Event) -> Unit) : this(
                itemList = itemList.map { Item(it, isSelected(it)) },
                eventSink = eventSink
            )

            data class Item(val value: File, val isSelected: Boolean)
        }

        object Loading : State
    }
}

public fun GalleryPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is GalleryScreen -> presenterOf { GalleryPresenter(rememberStorageManager(), navigator) }
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
internal fun GalleryPresenter(manager: StorageManager, navigator: Navigator): GalleryScreen.State {
    var itemList by remember { mutableStateOf(manager.list()) }
    var selected by remember { mutableStateOf(emptyList<File>()) }
    var takePhoto by remember { mutableStateOf(false) }

    if (takePhoto) {
        return GalleryScreen.State.Capture { event ->
            check(event is GalleryScreen.Event.Result)
            itemList += event.value
            takePhoto = false
        }
    }

    return GalleryScreen.State.Success(itemList, { it in selected }) { event ->
        when (event) {
            is GalleryScreen.Event.Capture -> takePhoto = true

            is GalleryScreen.Event.Delete -> {
                selected.forEach { manager.delete(it) }
                itemList = itemList - selected.toSet()
                selected = emptyList()
            }

            is GalleryScreen.Event.Toggle -> itemList[event.index].also {
                if (it in selected) selected -= it else selected += it
            }

            else -> throw IllegalArgumentException("$event")
        }
    }
}
