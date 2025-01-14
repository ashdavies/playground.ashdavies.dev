package io.ashdavies.party.past

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import io.ashdavies.aggregator.AsgConference
import io.ashdavies.aggregator.callable.PastConferencesCallable
import io.ashdavies.party.events.Event
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

@Composable
internal fun PastEventsPresenter(
    pastConferencesCallable: PastConferencesCallable,
): PastEventsScreen.State {
    val itemList by produceState(emptyList()) {
        value = pastConferencesCallable(Unit).map { it.toEvent() }
    }

    return PastEventsScreen.State(
        itemList = itemList.toImmutableList(),
    )
}

internal fun AsgConference.toEvent(): Event = Event(
    id = hash(), name = name, website = website, location = location, dateStart = dateStart,
    dateEnd = dateEnd, imageUrl = imageUrl, status = status, online = online,
    cfpStart = cfp?.start, cfpEnd = cfp?.end, cfpSite = cfp?.site,
)

private inline fun <reified T : Any> T.hash() = Json
    .encodeToString(this)
    .encode().md5().hex()
