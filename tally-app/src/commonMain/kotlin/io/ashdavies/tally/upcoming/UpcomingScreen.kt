package io.ashdavies.tally.upcoming

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import kotlinx.coroutines.launch
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
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = scaffoldDirective.copy(
            horizontalPartitionSpacerSize = 0.dp,
        ),
    )

    val coroutineScope = rememberCoroutineScope()
    val onBack by rememberUpdatedState<() -> Unit> {
        coroutineScope.launch { scaffoldNavigator.navigateBack() }
    }

    BackHandler(scaffoldNavigator.canNavigateBack(), onBack)

    ListDetailPaneScaffold(
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                UpcomingPane(
                    state = state,
                    onClick = {
                        coroutineScope.launch {
                            scaffoldNavigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = it,
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    EventsDetailPane(
                        item = requireNotNull(state.itemList[it]),
                        navigationIcon = {
                            FadeVisibility(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                BackButton(onBack)
                            }
                        },
                    )
                }
            }
        },
        modifier = modifier,
    )
}
