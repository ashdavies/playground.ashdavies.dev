package dev.ashdavies.playground.gallery

import com.slack.circuit.runtime.CircuitUiState

public data class GalleryScreenState(
    public val itemList: List<StandardItem>,
    public val expandedItem: ExpandedItem?,
    public val showCapture: Boolean,
    public val eventSink: (GalleryScreenEvent) -> Unit,
) : CircuitUiState {

    public data class StandardItem(
        public val title: String,
        public val imageModel: Any?,
        public val isSelected: Boolean,
        public val state: SyncState,
    )

    public data class ExpandedItem(
        public val contentDescription: String,
        public val imageModel: Any?,
        public val isExpanded: Boolean,
    )
}
