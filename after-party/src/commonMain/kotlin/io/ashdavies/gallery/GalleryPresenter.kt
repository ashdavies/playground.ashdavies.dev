package io.ashdavies.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.ashdavies.identity.IdentityManager
import io.ashdavies.identity.IdentityState
import kotlinx.coroutines.launch

@Composable
internal fun GalleryPresenter(
    imageManager: ImageManager,
    syncManager: SyncManager,
    identityManager: IdentityManager,
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
