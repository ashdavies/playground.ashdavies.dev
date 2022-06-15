package io.ashdavies.playground.cloud

import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.cloud.functions.HttpFunction
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.jetbrains.skiko.MainUIDispatcher

public fun HttpApplication(block: @Composable () -> Unit): HttpFunction = HttpFunction { request, response ->
    application {
        CompositionLocalProvider(
            LocalApplicationScope provides this,
            LocalHttpRequest provides request,
            LocalHttpResponse provides response,
        ) { block() }
    }
}

private fun application(content: @Composable ApplicationScope.() -> Unit) = runBlocking<Unit> {
    suspend fun compose(recomposer: Recomposer, content: @Composable () -> Unit) {
        val composition = Composition(EmptyApplier(), recomposer)
        try {
            composition.setContent(content)
            recomposer.close()
            recomposer.join()
        } finally {
            composition.dispose()
        }
    }

    withContext(MainUIDispatcher + YieldFrameClock) {
        val recomposer = Recomposer(coroutineContext)
        var isOpen by mutableStateOf(true)
        val scope = ApplicationScope {
            isOpen = false
        }

        launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        launch {
            compose(recomposer) { if (isOpen) scope.content() }
        }
    }
}

public fun interface ApplicationScope {
    fun exitApplication()
}

private object YieldFrameClock : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
        yield(); return onFrame(System.nanoTime())
    }
}

private class EmptyApplier : Applier<Unit> {
    override val current: Unit = Unit
    override fun down(node: Unit) = Unit
    override fun up() = Unit
    override fun insertTopDown(index: Int, instance: Unit) = Unit
    override fun insertBottomUp(index: Int, instance: Unit) = Unit
    override fun remove(index: Int, count: Int) = Unit
    override fun move(from: Int, to: Int, count: Int) = Unit
    override fun clear() = Unit
    override fun onEndChanges() = Unit
}
