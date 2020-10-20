package io.ashdavies.playground.conferences

import android.content.Context
import androidx.compose.foundation.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ContextAmbient
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.graph
import io.ashdavies.playground.invoke
import io.ashdavies.playground.network.Conference

@Preview
@Composable
internal fun ConferencesScreen(
    context: Context = ContextAmbient.current,
) {
    val conferencesRepository: ConferencesRepository =
        context.graph { conferencesRepository }

    val state: State<List<Conference>> =
        conferencesRepository
            .getAll()
            .collectAsState(emptyList())

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
