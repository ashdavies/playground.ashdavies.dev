package dev.ashdavies.playground.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
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
public fun retainCoroutineScope(
    context: CoroutineContext = Dispatchers.Main.immediate,
): CoroutineScope = retain(COROUTINE_SCOPE) {
    val coroutineScope = StableCoroutineScope(CoroutineScope(context + Job()))
    rememberObserver(coroutineScope::cancel)
    coroutineScope
}

private fun rememberObserver(onForgotten: () -> Unit) = object : RememberObserver {
    override fun onAbandoned() = onForgotten()
    override fun onForgotten() = onForgotten()
    override fun onRemembered() = Unit
}
