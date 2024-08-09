package io.ashdavies.party.gallery

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ashdavies.analytics.OnClick
import io.ashdavies.material.BottomSheetItem

@Composable
internal fun GallerySheetContent(
    eventSink: (GalleryScreen.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        BottomSheetItem(
            imageVector = Icons.Filled.Sync,
            onClick = OnClick("gallery_sync") {
                eventSink(GalleryScreen.Event.Selection.Sync)
            },
            contentDescription = "Sync",
            modifier = Modifier.padding(8.dp),
        )

        BottomSheetItem(
            imageVector = Icons.Filled.Delete,
            onClick = OnClick("gallery_delete") {
                eventSink(GalleryScreen.Event.Selection.Delete)
            },
            contentDescription = "Delete",
            modifier = Modifier.padding(8.dp),
        )
    }
}
