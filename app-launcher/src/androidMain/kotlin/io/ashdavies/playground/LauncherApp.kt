package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.compose.ProvideFirebaseApp

@Composable
internal fun LauncherApp(componentContext: ComponentContext, route: LauncherRoute?) {
    ProvideFirebaseApp { LauncherScreen(componentContext, route) }
}
