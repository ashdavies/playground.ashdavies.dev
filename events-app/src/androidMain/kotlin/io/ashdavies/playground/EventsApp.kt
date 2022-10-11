package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.compose.ProvideFirebaseApp
import io.ashdavies.http.LocalHttpClient

@Composable
internal fun EventsApp(componentContext: ComponentContext) {
    ProvideFirebaseApp {
        ProvideAppCheckToken(LocalHttpClient.current) {
            ComposeScreen {
                EventsRoot(componentContext)
            }
        }
    }
}
