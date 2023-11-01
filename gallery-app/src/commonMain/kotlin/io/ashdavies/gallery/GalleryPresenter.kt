@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package io.ashdavies.gallery

import androidx.compose.runtime.Composable
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
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
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

    internal data class State(
        val itemList: List<Item>,
        val showCapture: Boolean,
        val isLoggedIn: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        data class Item(
            val name: String,
            val file: File,
            val isSelected: Boolean,
            val state: SyncState,
        )
    }
}

public fun GalleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    val database = DatabaseFactory(PlaygroundDatabase.Schema, context) { PlaygroundDatabase(it) }
    val storage = StorageManager(PathProvider(context).images)
    val images = ImageManager(storage, database.imageQueries)

    val engine = InMemoryHttpClientEngine(emptyList())
    val sync = SyncManager(DefaultHttpClient(engine))

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
        images.list.collect { value = it }
    }

    var selected by remember { mutableStateOf(emptyList<Image>()) }
    var takePhoto by remember { mutableStateOf(false) }

    val syncState by produceState(emptyMap<String, SyncState>()) {
        sync.state.collect { value = it }
    }

    val coroutineScope = rememberCoroutineScope()

    return GalleryScreen.State(
        itemList = itemList.map {
            GalleryScreen.State.Item(
                name = it.name,
                file = File(it.path),
                isSelected = it in selected,
                state = syncState[it.name] ?: SyncState.NOT_SYNCED,
            )
        },
        showCapture = takePhoto,
        isLoggedIn = false,
    ) { event ->
        when (event) {
            is GalleryScreen.Event.Capture -> takePhoto = true

            is GalleryScreen.Event.Delete -> {
                selected.forEach { images.remove(it) }
                selected = emptyList()
            }

            is GalleryScreen.Event.Sync -> coroutineScope.launch {
                selected.forEach { sync.sync(it.path) }
                selected = emptyList()
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
