package dev.ashdavies.playground.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_SCOPE = "COROUTINE_SCOPE"

@Stable
internal class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
internal fun retainStableCoroutineScope(
    context: CoroutineContext = Dispatchers.Main,
): StableCoroutineScope = retain(COROUTINE_SCOPE) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onRetired: () -> Unit) = object : RetainObserver {
    override fun onRetained() = Unit

    override fun onEnteredComposition() = Unit
    override fun onExitedComposition() = Unit

    override fun onRetired() = onRetired()
    override fun onUnused() = onRetired()
}
