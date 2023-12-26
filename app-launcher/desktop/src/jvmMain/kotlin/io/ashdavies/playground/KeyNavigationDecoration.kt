package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onPlaced
import com.slack.circuit.backstack.NavDecoration
import com.slack.circuit.foundation.NavigatorDefaults
import kotlinx.collections.immutable.ImmutableList

internal class KeyNavigationDecoration(
    private val decoration: NavDecoration = NavigatorDefaults.DefaultDecoration,
    private val predicate: (KeyEvent) -> Boolean = { it.key == Key.Escape },
    private val onBackInvoked: () -> Unit,
) : NavDecoration {

    @Composable
    override fun <T> DecoratedContent(
        args: ImmutableList<T>,
        backStackDepth: Int,
        modifier: Modifier,
        content: @Composable (T) -> Unit
    ) {
        decoration.DecoratedContent(
            args = args,
            backStackDepth = backStackDepth,
            modifier = modifier
                .focusOnPlacement()
                .onPreviewKeyUp(
                    predicate = predicate,
                    action = onBackInvoked,
                ),
            content = content,
        )
    }
}

@Composable
private fun Modifier.focusOnPlacement(): Modifier {
    val focusRequester = remember { FocusRequester() }
    return focusRequester(focusRequester).onPlaced { focusRequester.requestFocus() }
}

@Composable
private fun Modifier.onPreviewKeyUp(
    predicate: (KeyEvent) -> Boolean = { true },
    action: () -> Unit,
): Modifier = onPreviewKeyEvent {
    if (it.type == KeyEventType.KeyUp && predicate(it)) {
        action()
        true
    } else {
        false
    }
}
