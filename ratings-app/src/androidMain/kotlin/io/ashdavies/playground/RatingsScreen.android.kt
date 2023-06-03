package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun RatingsScreen() {
    RatingsScreen(RatingsScreen.State.Idle(listOf(RatingsItem())) { })
}

@Preview
@Composable
internal fun RatingsPlaceholder() {
    RatingsScreen(RatingsScreen.State.Loading(3))
}

private fun RatingsItem() = RatingsItem(
    id = "${System.currentTimeMillis()}",
    name = "Lorem ipsum dolor sit amet",
    url = "https://ashdavies.dev/",
    score = 1500L,
)
