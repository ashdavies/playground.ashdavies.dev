package io.ashdavies.playground.google

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_OK
import kotlin.coroutines.CoroutineContext

private const val APPLICATION_JSON = "application/json"

@OptIn(DelicateCoroutinesApi::class)
abstract class CoroutineFunction(override val coroutineContext: CoroutineContext) : CoroutineScope, HttpFunction {
    final override fun service(request: HttpRequest, response: HttpResponse) {
        launch {
            runCatching { response.respond(service(request)) }.onFailure {
                response.setStatusCode(HTTP_INTERNAL_ERROR, it.message)
            }
        }
    }

    abstract suspend fun service(request: HttpRequest): String
}

private suspend fun HttpResponse.respond(json: String) = withContext(Dispatchers.IO) {
    setContentType(APPLICATION_JSON)
    setStatusCode(HTTP_OK)
    writer.write(json)
}
