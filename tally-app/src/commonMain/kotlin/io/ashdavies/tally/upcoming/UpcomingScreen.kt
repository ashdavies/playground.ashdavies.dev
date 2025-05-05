package io.ashdavies.tally.upcoming

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.foundation.internal.BackHandler
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.tally.animation.FadeVisibility
import io.ashdavies.tally.events.EventsDetailPane
import io.ashdavies.tally.material.BackButton
import kotlinx.collections.immutable.ImmutableList
import io.ashdavies.tally.events.Event as DatabaseEvent

@Parcelize
internal object UpcomingScreen : Parcelable, Screen {
    sealed interface Event {
        data object Refresh : Event
    }

    data class State(
        val itemList: ImmutableList<DatabaseEvent?>,
        val selectedIndex: Int?,
        val isRefreshing: Boolean,
        val errorMessage: String?,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal fun UpcomingEventsScreen(
    state: UpcomingScreen.State,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = calculatePaneScaffoldDirective(
            windowAdaptiveInfo = currentWindowAdaptiveInfo(),
        ).copy(horizontalPartitionSpacerSize = 0.dp),
    )

    BackHandler(navigator.canNavigateBack(BackNavigationBehavior.PopUntilContentChange)) {
        navigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                UpcomingPane(
                    state = state,
                    onClick = navigator::navigateToDetail,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    EventsDetailPane(
                        item = requireNotNull(state.itemList[it]),
                        navigationIcon = {
                            FadeVisibility(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                BackButton(navigator::navigateBack)
                            }
                        },
                    )
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
