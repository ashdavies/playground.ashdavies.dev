package dev.ashdavies.playground.event.grid

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
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.ashdavies.sql.mapAsFlow
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provider
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Third party concretions cannot be handled by WasmJs so a Provider interface must be used
 * Fixed with Metro 1.1.0 (https://github.com/ZacSweers/metro/pull/2279)
 */
@CircuitInject(EventScreen.Grid::class, AppScope::class)
internal class EventGridPresenter @Inject constructor(
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    private val httpClientProvider: Provider<HttpClient>,
) : Presenter<EventGridState> {

    @Composable
    override fun present(): EventGridState {
        val attendanceQueries = databaseFactory.map { it.attendanceQueries }
        val eventGridCallable = EventGridCallable(httpClientProvider())

        val eventList by produceState(emptyList()) {
            value = eventGridCallable(Unit).map {
                it to LocalDate.parse(it.dateStart)
            }
        }

        val attendanceList by attendanceQueries
            .mapAsFlow { it.selectAll { id, _ -> id } }
            .mapToList(Dispatchers.Default)
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

        val coroutineScope = rememberCoroutineScope()

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
