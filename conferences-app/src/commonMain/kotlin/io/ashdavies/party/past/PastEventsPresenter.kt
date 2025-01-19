package io.ashdavies.party.past

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.aggregator.callable.PastConferencesCallable
import io.ashdavies.party.events.AttendanceQueries
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

@Composable
internal fun PastEventsPresenter(
    pastConferencesCallable: PastConferencesCallable,
    attendanceQueries: AttendanceQueries,
    ioDispatcher: CoroutineDispatcher,
): PastEventsScreen.State {
    val attendanceList by attendanceQueries
        .selectAll { id, _ -> id }
        .asFlow()
        .mapToList(ioDispatcher)
        .collectAsState(emptyList())

    val itemList by produceState(emptyList(), attendanceList) {
        value = pastConferencesCallable(Unit).map {
            val startDate = LocalDate.parse(it.dateStart)
            val uuid = Json.encodeToString(it)
                .encode()
                .md5()
                .hex()

            PastEventsScreen.State.Item(
                uuid = uuid,
                title = "${it.name} ${startDate.year}",
                subtitle = it.location,
                group = "${startDate.year}",
                attended = uuid in attendanceList,
            )
        }
    }

    return PastEventsScreen.State(
        itemList = itemList.toImmutableList(),
    ) { event ->
        when (event) {
            is PastEventsScreen.Event.MarkAttendance -> when (event.value) {
                true -> attendanceQueries.insert(event.id, "${Clock.System.now()}")
                false -> attendanceQueries.delete(event.id)
            }
        }
    }
}
