package io.ashdavies.playground.conferences

import androidx.compose.foundation.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.network.Conference
import java.util.Date

@Preview
@Composable
internal fun ConferencesScreen(parser: DateParser = dateParser) = ConferencesList(
    data = listOf(StubConference(parser))
)

@Composable
internal fun ConferencesList(data: List<Conference>) {
    LazyColumnFor(items = data) {
        ConferenceItem(data = it)
    }
}

@Composable
internal fun ConferenceItem(data: Conference) {
    Text(text = data.name)
}

@Suppress("FunctionName")
private fun StubConference(parser: (String) -> Date) = Conference(
    name = "Droidcon EMEA",
    website = "https://www.online.droidcon.com/emea2020",
    location = "Online",
    dateStart = parser("2020-10-08"),
    dateEnd = parser("2020-10-09"),
    cfpStart = parser("2020-07-25"),
    cfpEnd = parser("2020-08-15"),
    cfpSite = "https://sessionize.com/droidconEMEA/"
)
