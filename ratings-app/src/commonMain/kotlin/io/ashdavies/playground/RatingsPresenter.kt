package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import java.net.URL

private const val DefaultSize = 3

@Composable
internal fun RatingsPresenter(navigator: Navigator): RatingsScreen.State {
    val state by produceState<RatingsScreen.State>(RatingsScreen.State.Loading(DefaultSize)) {
        val title = { _: String -> URL("https://api.github.com/zen").readText() }
        val items = List(DefaultSize) { RatingsScreen.State.Item("$it", title("$it")) }

        value = RatingsScreen.State.Idle(items) { event ->
            when (event) {
                RatingsScreen.Event.Pop -> navigator.pop()
                is RatingsScreen.Event.Details -> TODO()
                is RatingsScreen.Event.Ignore -> TODO()
                is RatingsScreen.Event.Vote -> TODO()
            }
        }
    }

    return state
}

@Parcelize
public object RatingsScreen : Parcelable, Screen {
    internal sealed interface Event : CircuitUiEvent {
        data class Details(val id: Int) : Event
        data class Ignore(val id: Int) : Event
        data class Vote(val id: Int) : Event
        object Pop : Event
    }

    internal sealed interface State : CircuitUiState {
        data class Idle(val items: List<Item>, val eventSink: (Event) -> Unit) : State
        data class Item(val id: String, val title: String)
        data class Loading(val size: Int) : State
        companion object
    }
}
