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
import androidx.compose.ui.window.ApplicationScope
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.jetbrains.skiko.MainUIDispatcher

private object HttpApplicationScope : HttpScope

public fun HttpApplication(block: @Composable HttpScope.() -> Unit): HttpFunction {
    return HttpApplication(HttpConfig.Get, block)
}

public fun HttpApplication(config: HttpConfig, block: @Composable HttpScope.() -> Unit): HttpFunction {
    return LocalHttpFunction(config) { request, response ->
        application {
            CompositionLocalProvider(
                LocalApplicationScope provides this,
                LocalHttpResponse provides response,
                LocalHttpRequest provides request,
                content = { block() },
            )
        }
    }
}

private fun LocalHttpFunction(config: HttpConfig, block: HttpScope.(HttpRequest, HttpResponse) -> Unit): HttpFunction {
    return HttpFunction { request, response ->
        when {
            request.contentLength == 0L -> response.setStatusCode(HttpStatusCode.BadRequest)

            request.contentType.get() != "${config.accept}" -> {
                response.appendHeader(HttpHeaders.Accept, "${config.accept}")
                response.setStatusCode(HttpStatusCode.UnsupportedMediaType)
            }

            request.method != config.allow.value -> {
                response.appendHeader(HttpHeaders.Allow, config.allow.value)
                response.setStatusCode(HttpStatusCode.MethodNotAllowed)
            }

            else -> runCatching { HttpApplicationScope.block(request, response) }.recover { throwable ->
                response.setStatusCode(HttpStatusCode.InternalServerError, throwable.message)
                response.writer.write(throwable.message ?: "Unknown error")
                throwable.printStackTrace()
            }
        }
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
        val scope = object : ApplicationScope {
            override fun exitApplication() {
                isOpen = false
            }
        }

        launch {
            recomposer.runRecomposeAndApplyChanges()
        }

        launch {
            compose(recomposer) { if (isOpen) scope.content() }
        }
    }
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
