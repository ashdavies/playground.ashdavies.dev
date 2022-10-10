package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.compose.ProvideFirebaseApp

@Composable
internal fun EventsApp(componentContext: ComponentContext) {
    ProvideFirebaseApp {
        ProvideAppCheckToken {
            ComposeScreen {
                EventsRoot(componentContext)
            }
        }
    }
}
