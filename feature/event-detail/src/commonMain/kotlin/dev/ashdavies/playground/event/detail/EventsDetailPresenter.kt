package dev.ashdavies.playground.event.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.event.common.PlaygroundDatabase
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.sql.invoke
import dev.ashdavies.sql.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

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
    @CircuitInject(EventScreen.Detail::class, AppScope::class)
    fun interface Factory {
        operator fun invoke(
            screen: EventScreen.Detail,
            navigator: Navigator,
        ): EventsDetailPresenter
    }
}
