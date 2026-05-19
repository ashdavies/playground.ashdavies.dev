package dev.ashdavies.playground.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.analytics.logEvent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

@Inject
@Composable
@Suppress("CyclomaticComplexMethod")
@CircuitInject(GalleryScreen::class, AppScope::class)
public fun GalleryPresenter(
    imageManager: ImageManager,
    syncManager: SyncManager,
    remoteAnalytics: RemoteAnalytics,
): GalleryScreenState {
    val itemList by imageManager.list.collectAsRetainedState(emptyList())
    val syncState by syncManager.state.collectAsRetainedState(emptyMap())
    val coroutineScope = rememberCoroutineScope()

    var expandedItem by rememberRetained { mutableStateOf<GalleryScreenState.ExpandedItem?>(null) }
    var selected by rememberRetained { mutableStateOf(emptyList<Image>()) }
    var takePhoto by rememberRetained { mutableStateOf(false) }

    return GalleryScreenState(
        itemList = itemList.map {
            GalleryScreenState.StandardItem(
                title = it.path.name,
                imageModel = it.path,
                isSelected = it in selected,
                state = syncState[it.uuid] ?: SyncState.NOT_SYNCED,
            )
        },
        expandedItem = expandedItem,
        showCapture = takePhoto,
    ) { event ->
        when (event) {
            is GalleryScreenEvent.Capture.Result -> coroutineScope.launch {
                imageManager.add(event.value)
                takePhoto = false
            }

            is GalleryScreenEvent.Capture.Cancel -> takePhoto = false

            is GalleryScreenEvent.Capture.Request -> {
                remoteAnalytics.logEvent("gallery_capture")
                takePhoto = true
            }

            is GalleryScreenEvent.Selection.Expand -> with(itemList[event.index]) {
                expandedItem = GalleryScreenState.ExpandedItem(
                    contentDescription = path.name,
                    imageModel = path,
                    isExpanded = true,
                )
            }

            is GalleryScreenEvent.Selection.Toggle -> itemList[event.index].also {
                if (it in selected) selected -= it else selected += it
            }

            is GalleryScreenEvent.Selection.Collapse -> {
                expandedItem = expandedItem?.copy(isExpanded = false)
            }

            is GalleryScreenEvent.Selection.Delete -> {
                remoteAnalytics.logEvent("gallery_delete")

                coroutineScope.launch {
                    selected.forEach { imageManager.remove(it) }
                    selected = emptyList()
                }
            }

            is GalleryScreenEvent.Selection.Sync -> {
                remoteAnalytics.logEvent("gallery_sync")

                coroutineScope.launch {
                    selected.forEach { syncManager.sync(it) }
                    selected = emptyList()
                }
            }
        }
    }
}
