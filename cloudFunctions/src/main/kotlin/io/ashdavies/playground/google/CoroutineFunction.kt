package io.ashdavies.playground.google

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_OK

private const val APPLICATION_JSON = "application/json"
private const val UNKNOWN_ERROR = "Unknown error"

@OptIn(DelicateCoroutinesApi::class)
abstract class CoroutineFunction : HttpFunction {
    final override fun service(request: HttpRequest, response: HttpResponse) = runBlocking {
        try {
            val result: String = service(request)
            response.setContentType(APPLICATION_JSON)
            response.setStatusCode(HTTP_OK)
            response.write(result)
        } catch (exception: Exception) {
            response.setStatusCode(HTTP_INTERNAL_ERROR, exception.message)
            response.write(exception.message ?: UNKNOWN_ERROR)
            exception.printStackTrace()
        }
    }

    abstract suspend fun service(request: HttpRequest): String
}

private fun HttpResponse.write(content: String) = writer.write(content)
