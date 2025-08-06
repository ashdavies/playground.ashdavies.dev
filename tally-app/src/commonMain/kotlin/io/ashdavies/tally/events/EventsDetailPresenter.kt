package io.ashdavies.tally.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.invoke
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.CircuitScreenKey

internal class EventsDetailPresenter @Inject constructor(
    @Assisted private val screen: EventsDetailScreen,
    @Assisted private val navigator: Navigator,
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
) : Presenter<EventsDetailScreen.State> {

    @Composable
    override fun present(): EventsDetailScreen.State {
        val itemState by produceState<EventsDetailScreen.State.ItemState>(
            initialValue = EventsDetailScreen.State.ItemState.Loading,
            key1 = screen.id,
        ) {
            val item = databaseFactory.map { it.eventsQueries }
                .invoke { it.getById(screen.id) }
                .executeAsOne()

            value = EventsDetailScreen.State.ItemState.Done(item)
        }

        return EventsDetailScreen.State(
            itemState = itemState,
            onBackPressed = {
                navigator.pop()
            },
        )
    }

    @AssistedFactory
    @CircuitScreenKey(EventsDetailScreen::class)
    @ContributesIntoMap(AppScope::class, binding<(Screen, Navigator) -> Presenter<*>>())
    interface Factory : (EventsDetailScreen, Navigator) -> EventsDetailPresenter
}
