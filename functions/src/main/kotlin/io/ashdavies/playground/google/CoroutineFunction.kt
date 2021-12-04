package io.ashdavies.playground.google

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val APPLICATION_JSON = "application/json"

@OptIn(DelicateCoroutinesApi::class)
abstract class CoroutineFunction(private val coroutineScope: CoroutineScope = GlobalScope) : HttpFunction {
    final override fun service(request: HttpRequest, response: HttpResponse) {
        coroutineScope.launch {
            runCatching { response.respond(service(request)) }.onFailure {
                response.setStatusCode(500, it.message)
            }
        }
    }

    abstract suspend fun service(request: HttpRequest): String
}

private suspend fun HttpResponse.respond(json: String) = withContext(Dispatchers.IO) {
    setContentType(APPLICATION_JSON)
    writer.write(json)
}
