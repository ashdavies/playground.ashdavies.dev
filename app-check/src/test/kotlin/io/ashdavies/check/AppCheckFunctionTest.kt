package io.ashdavies.check

import com.google.cloud.functions.invoker.runner.Invoker
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.BufferedReader
import java.net.ServerSocket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

private const val AUTOMATIC_PORT = 0
private const val SERVER_READY = "Started ServerConnector"

private val ClassPath get() = requireNotNull(System.getProperty("java.class.path"))
private val JavaHome get() = requireNotNull(System.getProperty("java.home"))
private val Java get() = "$JavaHome/bin/java"

internal class AppCheckFunctionTest {

    @Test
    fun `should ping server`() = startServer<AppCheckFunction> { client ->
        val response = client.get { parameter("Hello", "World") }

        assertEquals(HttpStatusCode.Forbidden, response.status)
    }
}

private inline fun <reified T> startServer(noinline action: suspend (client: HttpClient) -> Unit) {
    startServer(T::class.java, action)
}

private fun <T> startServer(kls: Class<T>, action: suspend (client: HttpClient) -> Unit) {
    val classPath: String = kls
        .protectionDomain
        .codeSource
        .location
        .path

    val serverSocket = ServerSocket(AUTOMATIC_PORT)
    val localPort = serverSocket.use { it.localPort }

    val environment: Map<String, String> = mapOf(
        "FUNCTION_TARGET" to kls.canonicalName,
        "FUNCTION_SIGNATURE_TYPE" to "http",
        "FUNCTION_CLASSPATH" to classPath,
        "K_SERVICE" to kls.name,
        "PORT" to "$localPort",
    )

    val latch = CountDownLatch(1)
    val process = ProcessBuilder()
        .command(listOf(Java, Invoker::class.java.name))
        .also { it.environment() += environment }
        .redirectErrorStream(true)
        .start()

    val reader: BufferedReader = process.inputStream.bufferedReader()
    reader.forEachLine { if (it == SERVER_READY) latch.countDown() }
    reader.forEachLine { println("BufferedReaderOutput: $it") }

    check(latch.await(5, TimeUnit.SECONDS)) {
        val message = "Server never became ready"
        process.destroy()
        message
    }

    val client = HttpClient {
        install(DefaultRequest) { url("http://localhost:$localPort/") }
        install(Logging) { level = LogLevel.HEADERS }
        install(ContentNegotiation) { json() }
    }

    runBlocking { action(client) }

    process.destroy()
}
