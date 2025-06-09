package io.ashdavies.tally.past

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.asg.callable.PastConferencesCallable
import io.ashdavies.tally.events.AttendanceQueries
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encodeUtf8

@Composable
internal fun PastPresenter(
    pastConferencesCallable: PastConferencesCallable,
    attendanceQueries: AttendanceQueries,
    ioDispatcher: CoroutineDispatcher,
): PastScreen.State {
    val attendanceList by attendanceQueries
        .selectAll { id, _ -> id }
        .asFlow()
        .mapToList(ioDispatcher)
        .collectAsState(emptyList())

    val itemList by produceState(emptyList(), attendanceList) {
        value = pastConferencesCallable(Unit).map {
            val startDate = LocalDate.parse(it.dateStart)
            val uuid = Json.encodeToString(it)
                .encodeUtf8()
                .md5()
                .hex()

            PastScreen.State.Item(
                uuid = uuid,
                title = "${it.name} ${startDate.year}",
                subtitle = it.location,
                group = "${startDate.year}",
                attended = uuid in attendanceList,
            )
        }
    }

    val coroutineScope = rememberCoroutineScope()

    return PastScreen.State(
        itemList = itemList.toImmutableList(),
    ) { event ->
        when (event) {
            is PastScreen.Event.MarkAttendance -> coroutineScope.launch {
                when (event.value) {
                    true -> attendanceQueries.insert(event.id, "${Clock.System.now()}")
                    false -> attendanceQueries.delete(event.id)
                }
            }
        }
    }
}
