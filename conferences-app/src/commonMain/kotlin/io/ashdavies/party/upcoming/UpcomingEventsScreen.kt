package io.ashdavies.party.upcoming

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.party.events.Event
import io.ashdavies.party.events.EventsDetailPane

@Parcelize
internal object UpcomingEventsScreen : Parcelable, Screen {
    data class State(val pagingItems: LazyPagingItems<Event>) : CircuitUiState
}

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun UpcomingEventsScreen(
    state: UpcomingEventsScreen.State,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Event>()

    BackHandler(navigator.canNavigateBack(BackNavigationBehavior.PopUntilContentChange)) {
        navigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                UpcomingEventsPane(
                    state = state,
                    onClick = navigator::navigateToDetail,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    EventsDetailPane(it)
                }
            }
        },
        modifier = modifier,
    )
}

@ExperimentalMaterial3AdaptiveApi
private fun <T> ThreePaneScaffoldNavigator<T>.navigateToDetail(content: T? = null) {
    navigateTo(ListDetailPaneScaffoldRole.Detail, content)
}

// TODO NavigableListDetailPaneScaffold
