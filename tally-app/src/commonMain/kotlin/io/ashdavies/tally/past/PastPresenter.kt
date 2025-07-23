package io.ashdavies.tally.past

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.sqldelight.coroutines.mapToList
import io.ashdavies.sql.Suspended
import io.ashdavies.sql.mapAsFlow
import io.ashdavies.tally.events.AttendanceQueries
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun PastPresenter(
    pastConferencesCallable: PastConferencesCallable,
    attendanceQueries: Suspended<AttendanceQueries>,
    coroutineContext: CoroutineContext,
): PastScreen.State {
    val attendanceList by attendanceQueries
        .mapAsFlow { it.selectAll { id, _ -> id } }
        .mapToList(coroutineContext)
        .collectAsState(emptyList())

    val itemList by produceState(emptyList(), attendanceList) {
        value = pastConferencesCallable(Unit).map {
            val startDate = LocalDate.parse(it.dateStart)

            PastScreen.State.Item(
                uuid = it.id,
                title = "${it.name} ${startDate.year}",
                subtitle = it.location,
                group = "${startDate.year}",
                attended = it.id in attendanceList,
            )
        }
    }

    val coroutineScope = rememberCoroutineScope()

    @OptIn(ExperimentalTime::class)
    return PastScreen.State(itemList.toImmutableList()) { event ->
        when (event) {
            is PastScreen.Event.MarkAttendance -> coroutineScope.launch {
                when (event.value) {
                    true -> attendanceQueries().insert(event.id, "${Clock.System.now()}")
                    false -> attendanceQueries().delete(event.id)
                }
            }
        }
    }
}
