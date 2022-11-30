package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext

public fun main() {
    singleWindowApplication(WindowState(size = DpSize(450.dp, 975.dp)), title = "Playground") {
        LauncherScreen(DefaultComponentContext(DefaultLifecycle()))
    }
}
