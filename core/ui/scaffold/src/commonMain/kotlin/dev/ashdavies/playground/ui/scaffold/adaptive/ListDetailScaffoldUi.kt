package dev.ashdavies.playground.ui.scaffold.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.ashdavies.playground.material.spacing
import dev.ashdavies.playground.material.uniform
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.nothing_selected
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Inject
@CircuitInject(ListDetailScaffoldScreen::class, AppScope::class)
internal class ListDetailScaffoldUi : Ui<ListDetailScaffoldScreen.State> {

    @Composable
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun Content(state: ListDetailScaffoldScreen.State, modifier: Modifier) {
        val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfoV2())
        val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Screen>(
            scaffoldDirective = scaffoldDirective.copy(
                horizontalPartitionSpacerSize = 0.dp,
            ),
        )

        val coroutineScope = rememberCoroutineScope()

        NavigationBackHandler(
            state = rememberNavigationEventState(NavigationEventInfo.None),
            isBackEnabled = true,
            onBackCompleted = {
                coroutineScope.launch { scaffoldNavigator.navigateBack() }
            },
        )

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
                    } ?: EmptyDetailPane()
                }
            },
            extraPane = {
                AnimatedPane {
                    val contentKey = scaffoldNavigator.currentDestination?.contentKey
                    if (contentKey is ListDetailScaffoldScreen.ExtraPaneScreen) {
                        CircuitContent(contentKey.extraScreen, circuitNavigator)
                    }
                }
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptyDetailPane(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .padding(MaterialTheme.spacing.large.uniform)
            .fillMaxSize(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(Res.string.nothing_selected),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
