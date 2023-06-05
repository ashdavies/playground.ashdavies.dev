package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

private val SAMPLE_ITEM = RatingsItem(
    name = "Keep it logically awesome.",
    url = "https://api.github.com/zen",
    id = randomUuid(),
    score = 1500L,
)

@Preview
@Composable
internal fun RatingsScreen() {
    RatingsScreen(RatingsScreen.State.Idle(listOf(SAMPLE_ITEM)) { })
}

@Preview
@Composable
internal fun RatingsPlaceholder() {
    RatingsScreen(RatingsScreen.State.Loading(3))
}
