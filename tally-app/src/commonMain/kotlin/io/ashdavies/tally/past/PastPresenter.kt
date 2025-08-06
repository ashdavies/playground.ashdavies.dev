package io.ashdavies.tally.past

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.sqldelight.coroutines.mapToList
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.map
import dev.ashdavies.sql.mapAsFlow
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@CircuitScreenKey(PastScreen::class)
@ContributesIntoMap(AppScope::class, binding<Presenter<*>>())
internal class PastPresenter @Inject constructor(
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    private val httpClient: HttpClient,
) : Presenter<PastScreen.State> {

    @Composable
    override fun present(): PastScreen.State {
        val attendanceQueries = databaseFactory.map { it.attendanceQueries }
        val pastConferencesCallable = PastConferencesCallable(httpClient)

        val attendanceList by attendanceQueries
            .mapAsFlow { it.selectAll { id, _ -> id } }
            .mapToList(Dispatchers.Default)
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
}
