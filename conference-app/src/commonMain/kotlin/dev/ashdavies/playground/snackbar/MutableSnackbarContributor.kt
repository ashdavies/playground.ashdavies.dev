package dev.ashdavies.playground.snackbar

import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

internal class MutableSnackbarContributor : SnackbarContributor {

    private val _source = Channel<SnackbarEvent>(Channel.BUFFERED)
    override val source = _source.receiveAsFlow()

    suspend fun send(visuals: SnackbarVisuals): SnackbarResult {
        val result = CompletableDeferred<SnackbarResult>()

        _source.send { event ->
            try {
                result.complete(event(visuals))
            } catch (cause: CancellationException) {
                result.cancel(cause)
                throw cause
            } catch (cause: Exception) {
                result.completeExceptionally(cause)
            }
        }

        return result.await()
    }
}
