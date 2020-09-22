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
import io.ashdavies.playground.network.Conference
import io.ashdavies.playground.util.DateParser

@Preview
@Composable
internal fun ConferencesScreen(
    context: Context = ContextAmbient.current,
    parser: DateParser = dateParser,
) {
    val state: State<List<Conference>> = context
        .conferencesRepository
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

@Suppress("FunctionName")
private fun StubConference(parser: (String) -> Long) = Conference(
    name = "Droidcon EMEA",
    website = "https://www.online.droidcon.com/emea2020",
    location = "Online",
    dateStart = parser("2020-10-08"),
    dateEnd = parser("2020-10-09"),
    cfpStart = parser("2020-07-25"),
    cfpEnd = parser("2020-08-15"),
    cfpSite = "https://sessionize.com/droidconEMEA/"
)
