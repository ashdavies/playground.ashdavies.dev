package dev.ashdavies.playground.snackbar

import androidx.compose.material3.SnackbarResult
import dev.ashdavies.playground.http.AppCheckTokenServer
import dev.ashdavies.playground.http.AppCheckTokenState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

@Inject
@ContributesIntoSet(AppScope::class)
internal class AppCheckSnackbarContributor(
    private val appCheckTokenServer: AppCheckTokenServer,
) : SnackbarContributor {

    override val source = flowOf<SnackbarEvent> { host ->
        val result = host(
            message = "AppCheck is required for desktop",
            actionLabel = "Authenticate",
        )

        if (result == SnackbarResult.ActionPerformed) {
            coroutineScope {
                launch(Dispatchers.IO) { appCheckTokenServer.start() }

                val urlString = appCheckTokenServer.state
                    .filterIsInstance<AppCheckTokenState.Started>()
                    .map { it.urlString }
                    .first()

                openUri(urlString)
            }
        }
    }
}

private suspend fun openUri(uri: String) = withContext(Dispatchers.IO) {
    Desktop.getDesktop().browse(URI(uri))
}
