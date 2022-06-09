package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.CoroutineScope
import java.net.HttpURLConnection

private const val APPLICATION_JSON = "application/json"
private const val UNKNOWN_ERROR = "Unknown error"

@Composable
public fun HttpEffect(response: HttpResponse = LocalHttpResponse.current, block: suspend CoroutineScope.() -> String) {
    LaunchedEffect(Unit) {
        try {
            val result: String = block()
            response.setContentType(APPLICATION_JSON)
            response.setStatusCode(HttpURLConnection.HTTP_OK)
            response.write(result)
        } catch (exception: HttpException) {
            response.setStatusCode(exception.code, exception.message)
            response.write(exception.message)
        } catch (exception: Exception) {
            response.setStatusCode(HttpURLConnection.HTTP_INTERNAL_ERROR, exception.message)
            response.write(exception.message ?: UNKNOWN_ERROR)
            exception.printStackTrace()
        }
    }
}

private fun HttpResponse.write(content: String) = writer.write(content)
