package dev.ashdavies.playground.gallery

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun GallerySheetContent(
    eventSink: (GalleryScreenEvent.Selection) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        BottomSheetItem(
            imageVector = Icons.Filled.Sync,
            onClick = { eventSink(GalleryScreenEvent.Selection.Sync) },
            contentDescription = "Sync",
            modifier = Modifier.padding(8.dp),
        )

        BottomSheetItem(
            imageVector = Icons.Filled.Delete,
            onClick = { eventSink(GalleryScreenEvent.Selection.Delete) },
            contentDescription = "Delete",
            modifier = Modifier.padding(8.dp),
        )
    }
}
