package io.ashdavies.tally.home

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
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
import io.ashdavies.tally.upcoming.UpcomingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Parcelize
internal object ListDetailScaffoldScreen : Parcelable, Screen {
    data object State : CircuitUiState
}

@CircuitScreenKey(ListDetailScaffoldScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class ListDetailScaffoldUi @Inject constructor(
    private val circuit: Circuit
) : Ui<ListDetailScaffoldScreen.State> {

    @Composable
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun Content(state: ListDetailScaffoldScreen.State, modifier: Modifier) {
        val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
        val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Long>(
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

        val circuitNavigator = remember(scaffoldNavigator) {
            ListDetailScaffoldNavigator(scaffoldNavigator, coroutineScope)
        }

        ListDetailPaneScaffold(
            directive = scaffoldNavigator.scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    val presenter = remember { circuit.presenter(UpcomingScreen, circuitNavigator) }
                    val ui = remember { circuit.ui(UpcomingScreen) } as? Ui<CircuitUiState>

                    ui?.Content(
                        state = presenter.present(),
                        modifier = Modifier,
                    ) ?: circuit.onUnavailableContent(
                        /* screen = */ UpcomingScreen,
                        /* modifier = */ modifier,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    scaffoldNavigator.currentDestination?.contentKey?.let { contentKey ->
                        val presenter = remember { circuit.presenter(EventsDetailScreen(contentKey), circuitNavigator) }
                        val ui = remember { circuit.ui(EventsDetailScreen(contentKey)) }

                        ui?.Content(
                            state = presenter.present(),
                            modifier = Modifier,
                        ) ?: circuit.onUnavailableContent(
                            /* screen = */ UpcomingScreen,
                            /* modifier = */ modifier,
                        )
                    }
                }
            },
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private class ListDetailScaffoldNavigator(
    private val scaffoldNavigator: ThreePaneScaffoldNavigator<Long>,
    private val coroutineScope: CoroutineScope,
) : Navigator by Navigator.NoOp {

    override fun goTo(screen: Screen): Boolean = when {
        screen is EventsDetailScreen -> {
            coroutineScope.launch { scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, screen.id) }
            true
        }

        else -> false
    }

    override fun pop(result: PopResult?): Screen? = when {
        scaffoldNavigator.canNavigateBack() -> {
            coroutineScope.launch { scaffoldNavigator.navigateBack() }
            scaffoldNavigator.currentDestination?.contentKey?.let {
                EventsDetailScreen(it)
            }
        }

        else -> null
    }
}
