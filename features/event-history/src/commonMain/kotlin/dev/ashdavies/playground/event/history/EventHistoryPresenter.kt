package dev.ashdavies.playground.event.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.sqldelight.coroutines.mapToList
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.event.common.PlaygroundDatabase
import dev.ashdavies.playground.metro.mapAsFlow
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.SuspendLazy
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

private val Event.thumbnailImageSeed: Long
    get() = "$name$location"
        .hashCode()
        .toLong()

@AssistedInject
@ExperimentalMetroCoroutinesApi
internal class EventHistoryPresenter(
    @Assisted private val screen: EventScreen.History,
    @Assisted private val navigator: Navigator,
    private val playgroundDatabase: SuspendLazy<PlaygroundDatabase>,
) : Presenter<EventHistoryState> {

    @Composable
    override fun present(): EventHistoryState {
        val itemList by playgroundDatabase
            .mapAsFlow { it.eventQueries.getAllWithNameDescending(screen.uuid) }
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toEventHistoryStateItem() } }
            .collectAsState(emptyList())

        return EventHistoryState(itemList.toPersistentList()) {
            when {
                it is EventHistoryState.Event.ItemClick -> navigator.goTo(EventScreen.Detail(itemList[it.index].uuid))
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EventScreen.History::class, AppScope::class)
    fun interface Factory {
        operator fun invoke(
            screen: EventScreen.History,
            navigator: Navigator,
        ): EventHistoryPresenter
    }
}

private fun Event.toEventHistoryStateItem() = EventHistoryState.Item(
    thumbnailImageSeed = thumbnailImageSeed,
    uuid = uuid,
    name = name,
    year = "${dateStart.year}",
)
