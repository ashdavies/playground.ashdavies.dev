package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import kotlinx.coroutines.launch

private const val DEFAULT_PAGE_SIZE = 3

@Composable
internal fun RatingsPresenter(
    navigator: Navigator,
    service: RatingsService = rememberRatingsService(),
    handler: UriHandler = LocalUriHandler.current,
): RatingsScreen.State {
    var itemList by remember { mutableStateOf(initialItemList(DEFAULT_PAGE_SIZE)) }
    val coroutineScope = rememberCoroutineScope()
    var index by remember { mutableStateOf(0) }

    if (itemList.any { it is RatingsScreen.State.Item.Loading }) {
        LaunchedEffect(Unit) {
            itemList = itemList.mapIsInstance { _: RatingsScreen.State.Item.Loading ->
                RatingsScreen.State.Item.Complete(service.next())
            }
        }
    }

    if (index == DEFAULT_PAGE_SIZE) {
        LaunchedEffect(Unit) {
            val sorted = itemList.filterIsInstance<RatingsScreen.State.Item.Complete>()
                .sortedBy { it.rank }
                .map { it.value }

            itemList = initialItemList(DEFAULT_PAGE_SIZE)
            service.rate(sorted)
            index = 0
        }
    }

    return RatingsScreen.State(itemList) { event ->
        when (event) {
            is RatingsScreen.Event.Details -> {
                handler.openUri(event.item.url)
            }

            is RatingsScreen.Event.Ignore -> {
                itemList = itemList.mapIsInstance { it: RatingsScreen.State.Item.Complete ->
                    if (it.value == event.item) RatingsScreen.State.Item.Loading else it
                }

                coroutineScope.launch {
                    service.ignore(event.item)
                }
            }

            is RatingsScreen.Event.Rate -> {
                itemList = itemList.mapIsInstance { it: RatingsScreen.State.Item.Complete ->
                    if (it.value == event.item) it.copy(rank = index++) else it
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
        data class Rate(val item: RatingsItem) : Event

        object Pop : Event
    }

    internal data class State(
        val itemList: List<Item>,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {

        sealed interface Item {

            data class Complete(
                val value: RatingsItem,
                val rank: Int = -1,
            ) : Item

            object Loading : Item
        }
    }
}

private fun initialItemList(size: Int): List<RatingsScreen.State.Item> {
    return List(size) { RatingsScreen.State.Item.Loading }
}

private inline fun <T, reified R> List<T>.mapIsInstance(transform: (R) -> T): List<T> = map {
    if (it is R) transform(it) else it
}
