@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.DefaultHttpClient
import io.ashdavies.playground.DatabaseFactory
import kotlinx.coroutines.launch

@Parcelize
public object GalleryScreen : Parcelable, Screen {
    internal sealed interface Event : CircuitUiEvent {
        data class Result(val value: File) : Event
        data class Toggle(val index: Int) : Event
        data object Capture : Event
        data object Delete : Event
        data object Sync : Event
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
                state: (Image) -> SyncState,
                showCapture: Boolean,
                eventSink: (Event) -> Unit,
            ) : this(
                itemList = itemList.map { image ->
                    Item(
                        name = image.name,
                        file = File(image.path),
                        isSelected = isSelected(image),
                        state = state(image),
                    )
                },
                showCapture = showCapture,
                eventSink = eventSink,
            )

            data class Item(
                val name: String,
                val file: File,
                val isSelected: Boolean,
                val state: SyncState,
            )
        }

        data object Loading : State
    }
}

public fun GalleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    val database = DatabaseFactory(PlaygroundDatabase.Schema, context) { PlaygroundDatabase(it) }
    val storage = StorageManager(PathProvider(context).images)
    val images = ImageManager(storage, database.imageQueries)
    val sync = SyncManager(DefaultHttpClient())

    return Presenter.Factory { screen, _, _ ->
        when (screen) {
            is GalleryScreen -> presenterOf { GalleryPresenter(images, sync) }
            else -> null
        }
    }
}

public fun GalleryUiFactory(context: PlatformContext): Ui.Factory {
    val storage = StorageManager(PathProvider(context).images)

    return Ui.Factory { screen, _ ->
        when (screen) {
            is GalleryScreen -> ui<GalleryScreen.State> { state, modifier ->
                GalleryScreen(
                    state = state,
                    manager = storage,
                    modifier = modifier,
                )
            }

            else -> null
        }
    }
}

@Composable
internal fun GalleryPresenter(
    images: ImageManager,
    sync: SyncManager,
): GalleryScreen.State {
    val itemList by produceState(emptyList<Image>(), images) {
        images.list().collect { value = it }
    }

    var selected by remember { mutableStateOf(emptyList<Image>()) }
    var takePhoto by remember { mutableStateOf(false) }

    val syncState by sync.state().collectAsState(emptyMap())
    val coroutineScope = rememberCoroutineScope()

    return GalleryScreen.State.Success(
        itemList = itemList,
        isSelected = { it in selected },
        state = { syncState[it.name] ?: SyncState.NOT_SYNCED },
        showCapture = takePhoto,
    ) { event ->
        when (event) {
            is GalleryScreen.Event.Capture -> takePhoto = true

            is GalleryScreen.Event.Delete -> {
                selected.forEach { images.remove(it) }
                selected = emptyList()
            }

            is GalleryScreen.Event.Sync -> coroutineScope.launch {
                selected.forEach { sync.sync(it.path) }
            }

            is GalleryScreen.Event.Result -> {
                images.add(event.value)
                takePhoto = false
            }

            is GalleryScreen.Event.Toggle -> itemList[event.index].also {
                if (it in selected) selected -= it else selected += it
            }
        }
    }
}
