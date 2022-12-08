package io.ashdavies.cloud

import com.google.cloud.functions.invoker.runner.Invoker
import io.ktor.client.HttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

private const val AUTOMATIC_PORT = 0

private const val SERVER_READY = "Started ServerConnector"
private const val HTTP_SIGNATURE = "http"

private val ClassPath get() = requireNotNull(System.getProperty("java.class.path"))
private val JavaHome get() = requireNotNull(System.getProperty("java.home"))

private val Java get() = "$JavaHome/bin/java"

@ExperimentalCoroutinesApi
public inline fun <reified T> startServer(noinline action: suspend (client: HttpClient) -> Unit) {
    startServer(T::class.java, action)
}

@ExperimentalCoroutinesApi
public fun <T> startServer(kls: Class<T>, action: suspend (client: HttpClient) -> Unit) {
    val serverSocket = ServerSocket(AUTOMATIC_PORT)
    val localPort = serverSocket.use { it.localPort }

    val environment: Map<String, String> = mapOf(
        "FUNCTION_SIGNATURE_TYPE" to HTTP_SIGNATURE,
        "FUNCTION_TARGET" to kls.canonicalName,
        "K_SERVICE" to kls.name,
        "PORT" to "$localPort",
    )

    val latch = CountDownLatch(1)
    val process = ProcessBuilder()
        .command(listOf(Java, "-classpath", ClassPath, Invoker::class.java.name))
        .also { it.environment() += environment }
        .redirectErrorStream(true)
        .start()

    thread {
        val input: InputStream = process.inputStream
        val reader: InputStreamReader = input.reader()

        reader.forEachLine {
            if (it.contains(SERVER_READY)) latch.countDown()
            println(it)
        }
    }

    check(latch.await(5, TimeUnit.SECONDS)) {
        val message = "Server never became ready"
        process.destroy()
        message
    }

    try {
        runBlocking {
            action(TestHttpClient("http://localhost:$localPort/"))
        }
    } finally {
        process.destroy()
    }
}
