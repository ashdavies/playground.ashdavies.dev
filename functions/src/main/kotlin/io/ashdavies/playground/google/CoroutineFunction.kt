package io.ashdavies.playground.google

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val APPLICATION_JSON = "application/json"

@OptIn(DelicateCoroutinesApi::class)
@Suppress("BlockingMethodInNonBlockingContext")
abstract class CoroutineFunction : HttpFunction {
    final override fun service(request: HttpRequest, response: HttpResponse) {
        GlobalScope.launch {
            try {
                response.setContentType(APPLICATION_JSON)
                response.writer.write(service(request))
            } catch (exception: Exception) {
                response.setStatusCode(500, exception.message)
            }
        }
    }

    abstract suspend fun service(request: HttpRequest): String
}
