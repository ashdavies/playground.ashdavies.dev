package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.Navigator

@Composable
internal fun DetailsPresenter(
    navigator: Navigator,
    cardsStore: CardsStore,
    boxSet: BoxSet,
): DominionScreen.BoxSetDetails.State {
    var expandedCard by remember { mutableStateOf<Card?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val cards by produceState(emptyList<Card>()) {
        value = cardsStore(boxSet.title)
        isLoading = false
    }

    return DominionScreen.BoxSetDetails.State(
        boxSet = boxSet,
        cards = cards,
        expandedCard = expandedCard,
        isLoading = isLoading,
    ) { event ->
        when (event) {
            is DominionScreen.BoxSetDetails.Event.ExpandCard -> {
                expandedCard = event.card
            }

            DominionScreen.BoxSetDetails.Event.Back -> {
                navigator.pop()
            }
        }
    }
}
