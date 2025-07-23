package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.Inject
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.analytics.logEvent
import kotlinx.coroutines.launch

internal class GalleryPresenter @Inject constructor(
    private val imageManager: ImageManager,
    private val syncManager: SyncManager,
    private val remoteAnalytics: RemoteAnalytics,
) : Presenter<GalleryScreen.State> {

    @Composable
    @Suppress("CyclomaticComplexMethod")
    override fun present(): GalleryScreen.State {
        val itemList by imageManager.list.collectAsRetainedState(emptyList())
        val syncState by syncManager.state.collectAsRetainedState(emptyMap())
        val coroutineScope = rememberCoroutineScope()

        var expandedItem by rememberRetained { mutableStateOf<GalleryScreen.State.ExpandedItem?>(null) }
        var selected by rememberRetained { mutableStateOf(emptyList<Image>()) }
        var takePhoto by rememberRetained { mutableStateOf(false) }

        return GalleryScreen.State(
            itemList = itemList.map {
                GalleryScreen.State.StandardItem(
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
                is GalleryScreen.Event.Capture.Result -> coroutineScope.launch {
                    imageManager.add(event.value)
                    takePhoto = false
                }

                is GalleryScreen.Event.Capture.Cancel -> takePhoto = false
                is GalleryScreen.Event.Capture.Request -> {
                    remoteAnalytics.logEvent("gallery_capture")
                    takePhoto = true
                }

                is GalleryScreen.Event.Selection.Expand -> with(itemList[event.index]) {
                    expandedItem = GalleryScreen.State.ExpandedItem(
                        contentDescription = path.name,
                        imageModel = path,
                        isExpanded = true,
                    )
                }

                is GalleryScreen.Event.Selection.Toggle -> itemList[event.index].also {
                    if (it in selected) selected -= it else selected += it
                }

                is GalleryScreen.Event.Selection.Collapse -> {
                    expandedItem = expandedItem?.copy(isExpanded = false)
                }

                is GalleryScreen.Event.Selection.Delete -> {
                    remoteAnalytics.logEvent("gallery_delete")

                    coroutineScope.launch {
                        selected.forEach { imageManager.remove(it) }
                        selected = emptyList()
                    }
                }

                is GalleryScreen.Event.Selection.Sync -> {
                    remoteAnalytics.logEvent("gallery_sync")

                    coroutineScope.launch {
                        selected.forEach { syncManager.sync(it) }
                        selected = emptyList()
                    }
                }
            }
        }
    }
}
