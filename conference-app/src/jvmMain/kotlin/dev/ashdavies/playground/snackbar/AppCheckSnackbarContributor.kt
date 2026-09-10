package dev.ashdavies.playground.snackbar

import androidx.compose.material3.SnackbarResult
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

@Inject
@ContributesIntoSet(AppScope::class)
internal class AppCheckSnackbarContributor() : SnackbarContributor {

    override val source = flowOf<SnackbarEvent> { host ->
        val result = host(
            message = "AppCheck is required for desktop",
            actionLabel = "Authenticate",
        )

        if (result == SnackbarResult.ActionPerformed) {
            error("Desktop Client AppCheck Not Implemented")
        }
    }
}

private suspend fun openUri(uri: String) = withContext(Dispatchers.IO) {
    Desktop.getDesktop().browse(URI(uri))
}
