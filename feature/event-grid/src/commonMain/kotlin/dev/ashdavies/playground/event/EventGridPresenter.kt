package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.sqldelight.coroutines.mapToList
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.event.common.PlaygroundDatabase
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.ashdavies.sql.mapAsFlow
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@CircuitInject(EventScreen.Grid::class, AppScope::class)
internal class EventGridPresenter @Inject constructor(
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    private val httpClient: HttpClient,
) : Presenter<EventGridState> {

    @Composable
    override fun present(): EventGridState {
        val attendanceQueries = databaseFactory.map { it.attendanceQueries }
        val eventGridCallable = EventGridCallable(httpClient)

        val attendanceList by attendanceQueries
            .mapAsFlow { it.selectAll { id, _ -> id } }
            .mapToList(Dispatchers.Default)
            .collectAsState(emptyList())

        val itemList by produceState(emptyList(), attendanceList) {
            value = eventGridCallable(Unit).map {
                val startDate = LocalDate.parse(it.dateStart)

                EventGridState.Item(
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
        return EventGridState(itemList.toImmutableList()) { event ->
            when (event) {
                is EventGridState.Event.MarkAttendance -> coroutineScope.launch {
                    when (event.value) {
                        true -> attendanceQueries().insert(event.id, "${Clock.System.now()}")
                        false -> attendanceQueries().delete(event.id)
                    }
                }
            }
        }
    }
}
