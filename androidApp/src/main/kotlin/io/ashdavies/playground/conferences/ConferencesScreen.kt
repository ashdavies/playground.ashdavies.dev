package io.ashdavies.playground.conferences

import android.content.Context
import androidx.compose.foundation.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.conferences.ConferencesViewModel.Companion.Factory
import io.ashdavies.playground.network.Conference

@Preview
@Composable
internal fun ConferencesScreen(
    context: Context = ContextAmbient.current,
) {
    val viewModel: ConferencesViewModel = viewModel(factory = Factory(context))

    val state = viewModel
        .viewState
        .collectAsState()

    ConferencesList(state.value)
}

@Composable
internal fun ConferencesList(data: List<Conference>) {
    LazyColumnFor(items = data) {
        ConferenceItem(data = it)
    }
}

@Composable
internal fun ConferenceItem(data: Conference) {
    Surface {
        Text(text = data.name)
    }
}
