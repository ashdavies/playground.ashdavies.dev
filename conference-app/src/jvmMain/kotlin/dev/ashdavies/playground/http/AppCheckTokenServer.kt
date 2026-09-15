package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.EngineConnectorConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private const val DEFAULT_HOST = "localhost"
private const val DEFAULT_PORT = 8080

internal interface AppCheckTokenServer {
    val state: Flow<AppCheckTokenState>
    suspend fun start()
}

internal sealed interface AppCheckTokenState {
    data object Idle : AppCheckTokenState

    data class Started(val urlString: String) : AppCheckTokenState
    data class Done(val token: AppCheckToken) : AppCheckTokenState
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class LocalAppCheckServer : AppCheckTokenServer {

    override val state = MutableStateFlow<AppCheckTokenState>(AppCheckTokenState.Idle)

    override suspend fun start() = state.update { value ->
        println("=== Updating appCheckServer state, existing value = ($value)")

        if (value is AppCheckTokenState.Idle) {
            val config = await(DEFAULT_HOST, DEFAULT_PORT) { state.value = AppCheckTokenState.Done(it) }
            val urlString = "${config.type.name}://${config.host}:${config.port}"

            AppCheckTokenState.Started(urlString)
        } else {
            value
        }
    }

    private suspend fun await(host: String, port: Int, onComplete: (AppCheckToken) -> Unit): EngineConnectorConfig {
        val deferred = CompletableDeferred<Unit>()

        val engine = embeddedServer(Netty, port, host) {
            install(ContentNegotiation, Configuration::json)

            routing {
                staticResources("/__firebase/init.json", null, "/__firebase/init.json")
                staticResources("/conference-app.js", null, "conference-app.js")
                staticResources("/", null, "index.html")

                post("/") {
                    onComplete(call.receive())
                    call.respondText("OK")
                    deferred.complete(Unit)
                }
            }
        }

        println("=== Starting server...")
        engine.startSuspend(wait = false)

        deferred.invokeOnCompletion {
            engine.stop(
                gracePeriodMillis = 1_000,
                timeoutMillis = 2_000,
            )
        }

        return engine.engineConfig
            .connectors
            .single()
    }
}
