package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun RatingsScreen() {
    RatingsScreen(RatingsScreen.State.Idle(listOf(RatingsScreen.State.Item())) { })
}

@Preview
@Composable
internal fun RatingsPlaceholder() {
    RatingsScreen(RatingsScreen.State.Loading(3))
}

private fun RatingsScreen.State.Companion.Item() = RatingsScreen.State.Item(
    id = "${System.currentTimeMillis()}",
    title = "Lorem ipsum dolor sit amet",
)
