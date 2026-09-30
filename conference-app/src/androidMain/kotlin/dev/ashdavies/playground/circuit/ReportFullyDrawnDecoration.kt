package dev.ashdavies.playground.circuit

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.NavDecoration
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.navigation.NavArgument
import com.slack.circuit.runtime.navigation.NavStackList

internal class ReportFullyDrawnDecoration(
    private val decoration: NavDecoration = NavigatorDefaults.EmptyDecoration,
    private val activity: Activity,
) : NavDecoration {

    @Composable
    override fun <T : NavArgument> DecoratedContent(
        args: NavStackList<T>,
        navigator: Navigator,
        modifier: Modifier,
        content: @Composable ((T) -> Unit),
    ) {
        decoration.DecoratedContent(
            args = args,
            navigator = navigator,
            modifier = modifier,
            content = content,
        )

        LaunchedEffect(Unit) {
            activity.reportFullyDrawn()
        }
    }
}
