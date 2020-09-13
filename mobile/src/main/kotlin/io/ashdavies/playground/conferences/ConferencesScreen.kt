package io.ashdavies.playground.conferences

import androidx.compose.foundation.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.network.Conference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date

@Preview
@Composable
internal fun ConferencesScreenPreview() = ConferencesScreen(
    data = flowOf(listOf(StubConference()))
)

@Composable
internal fun ConferencesScreen(data: Flow<List<Conference>>) {
    val state: State<List<Conference>> = data.collectAsState(
        initial = emptyList()
    )

    LazyColumnFor(items = state.value) {
        ConferenceItem(data = it)
    }
}

@Composable
internal fun ConferenceItem(data: Conference) {
    Text(text = data.name)
}

@Suppress("FunctionName")
private fun StubConference() = Conference(
    name = "Droidcon EMEA",
    website = "https://www.online.droidcon.com/emea2020",
    location = "Online",
    dateStart = Date("2020-10-08"),
    dateEnd = Date("2020-10-09"),
    cfpStart = Date("2020-07-25"),
    cfpEnd = Date("2020-08-15"),
    cfpSite = "https://sessionize.com/droidconEMEA/"
)
