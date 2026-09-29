package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.ashdavies.playground.event.detail.EventDetailState
import dev.ashdavies.playground.event.detail.EventDetailUi

@Preview
@Composable
private fun EventListUiSuccessSequencePreview(
    @PreviewParameter(EventPreviewParameterProvider::class, limit = 4) event: Event,
) {
    EventDetailUi(
        state = EventDetailState(
            itemState = EventDetailState.ItemState.Done(event),
            onBackPressed = { },
        ),
    )
}

@Composable
@PreviewLightDark
private fun EventListUiSuccessLightDarkPreview(
    @PreviewParameter(EventPreviewParameterProvider::class, limit = 1) event: Event,
) {
    EventDetailUi(
        state = EventDetailState(
            itemState = EventDetailState.ItemState.Done(event),
            onBackPressed = { },
        ),
    )
}

@Composable
@PreviewLightDark
private fun EventListUiLoadingPreview() {
    EventDetailUi(
        state = EventDetailState(
            itemState = EventDetailState.ItemState.Loading,
            onBackPressed = { },
        ),
    )
}
