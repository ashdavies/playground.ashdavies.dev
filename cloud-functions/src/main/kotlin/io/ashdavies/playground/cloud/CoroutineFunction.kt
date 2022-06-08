package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_OK

private const val APPLICATION_JSON = "application/json"
private const val UNKNOWN_ERROR = "Unknown error"

public abstract class CoroutineFunction : HttpFunction {
    final override fun service(request: HttpRequest, response: HttpResponse) = runBlocking {
        try {
            val result: String = service(request)
            response.setContentType(APPLICATION_JSON)
            response.setStatusCode(HTTP_OK)
            response.write(result)
        } catch (exception: HttpException) {
            response.setStatusCode(exception.code, exception.message)
            response.write(exception.message)
        } catch (exception: Exception) {
            response.setStatusCode(HTTP_INTERNAL_ERROR, exception.message)
            response.write(exception.message ?: UNKNOWN_ERROR)
            exception.printStackTrace()
        }
    }

    abstract suspend fun service(request: HttpRequest): String
}

public class HttpException(val code: Int, override val message: String, cause: Throwable? = null) : Exception(cause) {

    companion object {

        fun InvalidArgument(message: String, cause: Throwable? = null) =
            HttpException(HTTP_BAD_REQUEST, "InvalidArgument: $message", cause)

        fun Forbidden(message: String, cause: Throwable? = null): HttpException =
            HttpException(HTTP_FORBIDDEN, "Forbidden: $message", cause)
    }
}

private fun HttpResponse.write(content: String) = writer.write(content)
