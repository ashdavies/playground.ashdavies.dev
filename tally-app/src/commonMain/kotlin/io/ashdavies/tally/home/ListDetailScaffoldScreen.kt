package io.ashdavies.tally.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ashdavies.tally.events.EventsDetailScreen
import io.ashdavies.tally.events.EventsDetailUi
import io.ashdavies.tally.upcoming.UpcomingPane
import io.ashdavies.tally.upcoming.UpcomingPresenter
import io.ashdavies.tally.upcoming.UpcomingScreen
import kotlinx.coroutines.launch

@Parcelize
internal object ListDetailScaffoldScreen : Parcelable, Screen {
    data object State : CircuitUiState
}

@CircuitScreenKey(ListDetailScaffoldScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class ListDetailScaffoldUi @Inject constructor(
    private val upcomingPresenter: UpcomingPresenter,
    private val eventsDetailUi: EventsDetailUi,
) : Ui<ListDetailScaffoldScreen.State> {

    @Composable
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun Content(state: ListDetailScaffoldScreen.State, modifier: Modifier) {
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

        @OptIn(ExperimentalComposeUiApi::class)
        BackHandler(scaffoldNavigator.canNavigateBack(), onBack)

        var lastUpcomingState by rememberRetained {
            mutableStateOf<UpcomingScreen.State?>(null)
        }

        ListDetailPaneScaffold(
            directive = scaffoldNavigator.scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    UpcomingPane(
                        state = upcomingPresenter.present().also {
                            lastUpcomingState = it
                        },
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
                        eventsDetailUi.Content(
                            EventsDetailScreen.State(
                                itemState = EventsDetailScreen.State.ItemState.Done(
                                    item = requireNotNull(lastUpcomingState?.itemList[it]),
                                ),
                                onBackPressed = onBack,
                            ),
                            modifier = Modifier,
                        )
                    }
                }
            },
            modifier = modifier,
        )
    }
}
