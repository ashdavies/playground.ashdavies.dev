package io.ashdavies.tally.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
internal class ListDetailScaffoldNavigator(
    private val scaffoldNavigator: ThreePaneScaffoldNavigator<Screen>,
    private val coroutineScope: CoroutineScope,
) : Navigator by Navigator.NoOp {

    override fun goTo(screen: Screen): Boolean {
        coroutineScope.launch { scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, screen) }
        return true
    }

    override fun pop(result: PopResult?): Screen? {
        if (scaffoldNavigator.canNavigateBack()) {
            val screen = scaffoldNavigator.currentDestination?.contentKey
            coroutineScope.launch { scaffoldNavigator.navigateBack() }
            return screen
        }

        return null
    }
}
