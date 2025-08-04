package io.ashdavies.tally.upcoming

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import kotlinx.collections.immutable.ImmutableList
import io.ashdavies.tally.events.Event as DbConference

@Parcelize
internal object UpcomingScreen : Parcelable, Screen {
    sealed interface Event {
        data object Refresh : Event
    }

    data class State(
        val itemList: ImmutableList<DbConference?>,
        val selectedIndex: Int?,
        val isRefreshing: Boolean,
        val errorMessage: String?,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}
