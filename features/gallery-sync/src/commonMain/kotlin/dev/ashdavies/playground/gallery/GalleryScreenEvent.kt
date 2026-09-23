package dev.ashdavies.playground.gallery

import com.slack.circuit.runtime.CircuitUiEvent

public sealed interface GalleryScreenEvent : CircuitUiEvent {
    public sealed interface Capture : GalleryScreenEvent {
        public data class Result(val value: Path) : Capture

        public data object Cancel : Capture
        public data object Request : Capture
    }

    public sealed interface Selection : GalleryScreenEvent {
        public data class Expand(val index: Int) : Selection
        public data class Toggle(val index: Int) : Selection

        public data object Collapse : Selection
        public data object Delete : Selection
        public data object Sync : Selection
    }
}
