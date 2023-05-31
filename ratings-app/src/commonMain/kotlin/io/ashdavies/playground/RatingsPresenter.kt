package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen

private const val NotionPath = "https://www.notion.so/ashdavies/%s"
private const val DEFAULT_PATH_SIZE = 3

@Composable
internal fun RatingsPresenter(
    navigator: Navigator,
    service: RatingsService = NotionRatingsService(),
    provider: RatingsProvider = RatingsProvider(),
    handler: UriHandler = LocalUriHandler.current,
): RatingsScreen.State {
    val state by produceState<RatingsScreen.State>(RatingsScreen.State.Loading(DEFAULT_PATH_SIZE)) {
        val page = service.next(DEFAULT_PATH_SIZE)

        value = RatingsScreen.State.Idle(page) { event ->
            when (event) {
                is RatingsScreen.Event.Vote -> provider.vote(page.sortedBy { it == event.item })
                is RatingsScreen.Event.Details -> handler.openUri(NotionPath.format(event.item))
                is RatingsScreen.Event.Ignore -> provider.ignore(event.item)
                RatingsScreen.Event.Pop -> navigator.pop()
            }
        }
    }

    return state
}

@Parcelize
public object RatingsScreen : Parcelable, Screen {

    internal sealed interface Event : CircuitUiEvent {

        data class Details(val item: RatingsItem) : Event
        data class Ignore(val item: RatingsItem) : Event
        data class Vote(val item: RatingsItem) : Event

        object Pop : Event
    }

    internal sealed interface State : CircuitUiState {

        data class Idle(
            val items: List<RatingsItem>,
            val eventSink: (Event) -> Unit,
        ) : State

        data class Loading(val size: Int) : State

        companion object
    }
}
