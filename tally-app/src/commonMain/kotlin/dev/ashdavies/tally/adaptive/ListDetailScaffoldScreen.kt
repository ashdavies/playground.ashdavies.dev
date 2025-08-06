package dev.ashdavies.tally.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize
import dev.ashdavies.tally.circuit.CircuitScreenKey
import kotlinx.coroutines.launch

@Parcelize
internal data class ListDetailScaffoldScreen(val initialScreen: Screen) : Parcelable, Screen {
    data class State(val initialScreen: Screen) : CircuitUiState
}

@CircuitScreenKey(ListDetailScaffoldScreen::class)
@ContributesIntoMap(AppScope::class, binding<Ui<*>>())
internal class ListDetailScaffoldUi @Inject constructor() : Ui<ListDetailScaffoldScreen.State> {

    @Composable
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun Content(state: ListDetailScaffoldScreen.State, modifier: Modifier) {
        val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
        val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Screen>(
            scaffoldDirective = scaffoldDirective.copy(
                horizontalPartitionSpacerSize = 0.dp,
            ),
        )

        val coroutineScope = rememberCoroutineScope()

        @OptIn(ExperimentalComposeUiApi::class)
        BackHandler(scaffoldNavigator.canNavigateBack()) {
            coroutineScope.launch { scaffoldNavigator.navigateBack() }
        }

        val circuitNavigator = remember(scaffoldNavigator) {
            ListDetailScaffoldNavigator(
                scaffoldNavigator = scaffoldNavigator,
                coroutineScope = coroutineScope,
            )
        }

        ListDetailPaneScaffold(
            directive = scaffoldNavigator.scaffoldDirective,
            value = scaffoldNavigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    CircuitContent(state.initialScreen, circuitNavigator)
                }
            },
            detailPane = {
                AnimatedPane {
                    scaffoldNavigator.currentDestination?.contentKey?.let { contentKey ->
                        CircuitContent(contentKey, circuitNavigator)
                    }
                }
            },
            modifier = modifier,
        )
    }
}
