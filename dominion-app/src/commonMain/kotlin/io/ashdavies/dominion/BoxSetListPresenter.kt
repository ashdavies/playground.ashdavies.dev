package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.Navigator


@Composable
internal fun BoxSetListPresenter(
    navigator: Navigator,
    boxSetStore: BoxSetStore,
): DominionScreen.BoxSetList.State {
    var isLoading by remember { mutableStateOf(true) }
    val boxSetList by produceState(emptyList<BoxSet>()) {
        value = boxSetStore()
        isLoading = false
    }

    return DominionScreen.BoxSetList.State(
        boxSetList = boxSetList,
        isLoading = isLoading,
    ) { event ->
        when (event) {
            is DominionScreen.BoxSetList.Event.ShowBoxSet -> {
                navigator.goTo(DominionScreen.BoxSetDetails(event.boxSet.title))
            }
        }
    }
}
