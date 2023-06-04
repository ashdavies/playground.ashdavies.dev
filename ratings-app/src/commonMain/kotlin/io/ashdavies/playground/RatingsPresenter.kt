package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import io.ashdavies.http.LocalHttpClient

private const val NotionPath = "https://www.notion.so/ashdavies/%s"
private const val DEFAULT_PAGE_SIZE = 3

@Composable
internal fun RatingsPresenter(
    navigator: Navigator,
    service: RatingsService = RatingsService(LocalHttpClient.current),
    provider: RatingsProvider = RatingsProvider(),
    handler: UriHandler = LocalUriHandler.current,
): RatingsScreen.State {
    var items by remember { mutableStateOf(List<RatingsScreen.State.Item>(DEFAULT_PAGE_SIZE) { RatingsScreen.State.Item.Loading }) }
    val loading by derivedStateOf { items.count { it is RatingsScreen.State.Item.Loading } }

    LaunchedEffect(loading) {
        if (loading > 0) {
            val next = ArrayDeque(service.next(loading))

            items = items.map {
                when (it) {
                    is RatingsScreen.State.Item.Loading -> RatingsScreen.State.Item.Complete(next.removeFirst())
                    else -> it
                }
            }
        }
    }

    return RatingsScreen.State(items) { event ->
        when (event) {
            is RatingsScreen.Event.Details -> handler.openUri(NotionPath.format(event.item))
            is RatingsScreen.Event.Ignore -> provider.ignore(event.item)
            is RatingsScreen.Event.Vote -> {
                val sorted = items
                    .filterIsInstance<RatingsScreen.State.Item.Complete>()
                    .sortedBy { it.item == event.item }
                    .map { it.item }

                provider.vote(sorted)

                items = items.map {
                    when {
                        it is RatingsScreen.State.Item.Complete && it.item == event.item -> RatingsScreen.State.Item.Loading
                        else -> it
                    }
                }
            }

            RatingsScreen.Event.Pop -> navigator.pop()
        }
    }
}

@Parcelize
public object RatingsScreen : Parcelable, Screen {

    internal sealed interface Event : CircuitUiEvent {

        data class Details(val item: RatingsItem) : Event
        data class Ignore(val item: RatingsItem) : Event
        data class Vote(val item: RatingsItem) : Event

        object Pop : Event
    }

    internal data class State(
        val itemList: List<Item>,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        sealed interface Item {

            data class Complete(val item: RatingsItem) : Item

            object Loading : Item
        }
    }
}
