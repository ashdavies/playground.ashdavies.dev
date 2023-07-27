package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
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
        data object Capture : Event
        data object Delete : Event
    }

    internal sealed interface State : CircuitUiState {
        data class Empty(val eventSink: (Event) -> Unit) : State

        data class Success(
            val itemList: List<Item>,
            val showCapture: Boolean,
            val eventSink: (Event) -> Unit,
        ) : State {
            constructor(
                itemList: List<Image>,
                isSelected: (Image) -> Boolean,
                showCapture: Boolean,
                eventSink: (Event) -> Unit,
            ) : this(
                itemList = itemList.map { Item(it, isSelected(it)) },
                showCapture = showCapture,
                eventSink = eventSink,
            )

            data class Item(
                val name: String,
                val file: File,
                val isSelected: Boolean,
            ) {
                constructor(image: Image, isSelected: Boolean) : this(
                    name = image.name,
                    file = File(image.path),
                    isSelected = isSelected,
                )
            }
        }

        data object Loading : State
    }
}

public fun GalleryPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, _, _ ->
        when (screen) {
        is GalleryScreen -> presenterOf { GalleryPresenter(rememberImageManager()) }
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
internal fun GalleryPresenter(manager: ImageManager): GalleryScreen.State {
    var selected by remember { mutableStateOf(emptyList<Image>()) }
    val itemList by manager.list().collectAsState(emptyList())
    var takePhoto by remember { mutableStateOf(false) }

    return GalleryScreen.State.Success(
        itemList = itemList,
        isSelected = { it in selected },
        showCapture = takePhoto,
    ) { event ->
        when (event) {
            is GalleryScreen.Event.Capture -> takePhoto = true

            is GalleryScreen.Event.Delete -> {
                selected.forEach { manager.remove(it) }
                selected = emptyList()
            }

            is GalleryScreen.Event.Result -> {
                manager.add(event.value)
                takePhoto = false
            }

            is GalleryScreen.Event.Toggle -> itemList[event.index].also {
                if (it in selected) selected -= it else selected += it
            }
        }
    }
}
