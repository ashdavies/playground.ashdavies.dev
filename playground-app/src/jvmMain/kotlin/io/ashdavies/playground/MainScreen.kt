package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext

public fun MainScreen(title: String = "Playground", content: @Composable (ComponentContext) -> Unit) {
    singleWindowApplication(WindowState(size = DpSize(450.dp, 975.dp)), title = title) {
        AppCheck {
            content(DefaultComponentContext(DefaultLifecycle()))
        }
    }
}
