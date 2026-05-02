package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@CircuitInject(EventScreen.Grid::class, AppScope::class)
internal class EventGridPresenter @Inject constructor(
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    private val httpClient: HttpClient,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
) : Presenter<EventGridState> {

    @Composable
    override fun present(): EventGridState {
        val attendanceQueries = databaseFactory.map { it.attendanceQueries }
        val eventGridCallable = EventGridCallable(httpClient)

        val eventList by produceState(emptyList()) {
            value = eventGridCallable(Unit).map {
                it to LocalDate.parse(it.dateStart)
            }
        }

        val attendanceList by attendanceQueries
            .mapAsFlow { it.selectAll { id, _ -> id } }
            .mapToList(coroutineContext)
            .map { it.toSet() }
            .collectAsState(emptySet())

        val itemList = remember(eventList, attendanceList) {
            eventList.map {
                EventGridState.Item(
                    uuid = it.first.id,
                    title = "${it.first.name} ${it.second.year}",
                    subtitle = it.first.location,
                    group = "${it.second.year}",
                    attended = it.first.id in attendanceList,
                )
            }.toImmutableList()
        }

        val coroutineScope = rememberCoroutineScope { coroutineContext }

        @OptIn(ExperimentalTime::class)
        return EventGridState(itemList) { event ->
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
