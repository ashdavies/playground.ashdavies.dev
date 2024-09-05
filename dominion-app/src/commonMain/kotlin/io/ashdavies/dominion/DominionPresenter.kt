package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
internal fun DominionPresenter(
    boxSetStore: BoxSetStore,
    cardsStore: CardsStore,
): DominionScreen.AdaptiveList.State {
    var isLoading by remember { mutableStateOf(true) }
    val boxSetList by produceState(emptyList<BoxSet>()) {
        value = boxSetStore()
        isLoading = false
    }

    return DominionScreen.AdaptiveList.State(
        boxSetList = boxSetList,
        cardList = { cardsStore(it.title) },
        isLoading = isLoading,
    )
}
