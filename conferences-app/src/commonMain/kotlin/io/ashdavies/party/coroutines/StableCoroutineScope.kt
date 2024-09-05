package io.ashdavies.party.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

private const val COROUTINE_SCOPE = "COROUTINE_SCOPE"

@Stable
internal class StableCoroutineScope(scope: CoroutineScope) : CoroutineScope by scope

@Composable
internal fun rememberRetainedCoroutineScope(
    context: CoroutineContext = Dispatchers.Main.immediate,
): StableCoroutineScope = rememberRetained(COROUTINE_SCOPE) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onForgotten: () -> Unit) = object : RememberObserver {
    override fun onAbandoned() = onForgotten()
    override fun onForgotten() = onForgotten()
    override fun onRemembered() = Unit
}
