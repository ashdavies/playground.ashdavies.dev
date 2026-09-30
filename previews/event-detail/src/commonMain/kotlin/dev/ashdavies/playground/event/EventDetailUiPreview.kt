package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.ashdavies.playground.event.detail.EventDetailState
import dev.ashdavies.playground.event.detail.EventDetailUi
import dev.ashdavies.playground.tooling.MaterialThemeWrapper

@Preview
@Composable
@PreviewWrapper(MaterialThemeWrapper::class)
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
@PreviewWrapper(MaterialThemeWrapper::class)
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
@PreviewWrapper(MaterialThemeWrapper::class)
private fun EventListUiLoadingPreview() {
    EventDetailUi(
        state = EventDetailState(
            itemState = EventDetailState.ItemState.Loading,
            onBackPressed = { },
        ),
    )
}
