package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.check.ProvideFirebaseApp

@Composable internal fun EventsApp(componentContext: ComponentContext) {
    ProvideFirebaseApp {
        ComposeScreen {
            EventsRoot(componentContext)
        }
    }
}
