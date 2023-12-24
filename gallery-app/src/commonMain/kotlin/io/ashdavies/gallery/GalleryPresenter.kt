@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import io.ashdavies.playground.DatabaseFactory
import kotlinx.coroutines.launch

@Parcelize
public object GalleryScreen : Parcelable, Screen {
    internal sealed interface Event : CircuitUiEvent {
        sealed interface Capture : Event {
            data class Result(val value: File) : Capture

            data object Cancel : Capture
            data object Request : Capture
        }

        sealed interface Identity : Event {
            data object SignIn : Identity
        }

        sealed interface Selection : Event {
            data class Expand(val index: Int) : Selection
            data class Toggle(val index: Int) : Selection

            data object Collapse : Selection
            data object Delete : Selection
            data object Sync : Selection
        }
    }

    internal data class State(
        val itemList: List<StandardItem> = emptyList(),
        val expandedItem: ExpandedItem? = null,
        val showCapture: Boolean = false,
        val identityState: IdentityState,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        data class StandardItem(
            val title: String,
            val imageModel: Any?,
            val isSelected: Boolean,
            val state: SyncState,
        )

        data class ExpandedItem(
            val contentDescription: String,
            val imageModel: Any?,
            val isExpanded: Boolean,
        )
    }
}

public fun GalleryPresenterFactory(context: PlatformContext): Presenter.Factory {
    val database = DatabaseFactory(PlaygroundDatabase.Schema, context) { PlaygroundDatabase(it) }
    val imageManager = ImageManager(StorageManager(PathProvider(context)), database.imageQueries)
    val syncManager = SyncManager(DefaultHttpClient(InMemoryHttpClientEngine(emptyList())))
    val identityManager = IdentityManager(database.credentialQueries)

    return Presenter.Factory { screen, _, _ ->
        when (screen) {
            is GalleryScreen -> presenterOf {
                GalleryPresenter(
                    identityManager = identityManager,
                    imageManager = imageManager,
                    syncManager = syncManager,
                )
            }

            else -> null
        }
    }
}

public fun GalleryUiFactory(context: PlatformContext): Ui.Factory {
    val storage = StorageManager(PathProvider(context))

    return Ui.Factory { screen, _ ->
        when (screen) {
            is GalleryScreen -> ui<GalleryScreen.State> { state, modifier ->
                GalleryScreen(state, storage, modifier)
            }

            else -> null
        }
    }
}

@Composable
internal fun GalleryPresenter(
    identityManager: IdentityManager,
    imageManager: ImageManager,
    syncManager: SyncManager,
): GalleryScreen.State {
    val identityState by identityManager.state.collectAsState(IdentityState.Unsupported)
    val itemList by imageManager.list.collectAsState(emptyList())
    val syncState by syncManager.state.collectAsState(emptyMap())

    val coroutineScope = rememberCoroutineScope()

    var expandedItem by remember { mutableStateOf<GalleryScreen.State.ExpandedItem?>(null) }
    var selected by remember { mutableStateOf(emptyList<Image>()) }
    var takePhoto by remember { mutableStateOf(false) }

    return GalleryScreen.State(
        itemList = itemList.map {
            GalleryScreen.State.StandardItem(
                title = it.name,
                imageModel = File(it.path),
                isSelected = it in selected,
                state = syncState[it.name] ?: SyncState.NOT_SYNCED,
            )
        },
        expandedItem = expandedItem,
        showCapture = takePhoto,
        identityState = identityState,
    ) { event ->
        when (event) {
            is GalleryScreen.Event.Capture -> when (event) {
                is GalleryScreen.Event.Capture.Result -> coroutineScope.launch {
                    imageManager.add(event.value)
                    takePhoto = false
                }

                is GalleryScreen.Event.Capture.Cancel -> takePhoto = false
                is GalleryScreen.Event.Capture.Request -> takePhoto = true
            }

            is GalleryScreen.Event.Identity -> when (event) {
                is GalleryScreen.Event.Identity.SignIn -> coroutineScope.launch {
                    identityManager.signIn()
                }
            }

            is GalleryScreen.Event.Selection -> when (event) {
                is GalleryScreen.Event.Selection.Expand -> {
                    expandedItem = GalleryScreen.State.ExpandedItem(
                        contentDescription = itemList[event.index].name,
                        imageModel = File(itemList[event.index].path),
                        isExpanded = true,
                    )
                }

                is GalleryScreen.Event.Selection.Toggle -> itemList[event.index].also {
                    if (it in selected) selected -= it else selected += it
                }

                is GalleryScreen.Event.Selection.Collapse -> {
                    expandedItem = expandedItem?.copy(isExpanded = false)
                }

                is GalleryScreen.Event.Selection.Delete -> coroutineScope.launch {
                    selected.forEach { imageManager.remove(it) }
                    selected = emptyList()
                }

                is GalleryScreen.Event.Selection.Sync -> coroutineScope.launch {
                    selected.forEach { syncManager.sync(it.path) }
                    selected = emptyList()
                }
            }
        }
    }
}
