package dev.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.circuit.CircuitScreenKey
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.invoke
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding

internal class EventsDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: EventScreen.Detail,
    @Assisted private val navigator: Navigator,
    private val databaseFactory: DatabaseFactory<PlaygroundDatabase>,
) : Presenter<EventDetailState> {

    @Composable
    override fun present(): EventDetailState {
        val itemState by produceState<EventDetailState.ItemState>(
            initialValue = EventDetailState.ItemState.Loading,
            key1 = screen.id,
        ) {
            val item = databaseFactory.map { it.eventQueries }
                .invoke { it.getById(screen.id) }
                .executeAsOne()

            value = EventDetailState.ItemState.Done(item)
        }

        return EventDetailState(
            itemState = itemState,
            onBackPressed = {
                navigator.pop()
            },
        )
    }

    @AssistedFactory
    @CircuitScreenKey(EventScreen.Detail::class)
    @ContributesIntoMap(AppScope::class, binding<(Screen, Navigator) -> Presenter<*>>())
    interface Factory : (EventScreen.Detail, Navigator) -> EventsDetailPresenter {
        override operator fun invoke(screen: EventScreen.Detail, navigator: Navigator): EventsDetailPresenter
    }
}
