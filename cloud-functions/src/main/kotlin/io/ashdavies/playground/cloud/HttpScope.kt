package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.cloud.functions.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope

private const val UNKNOWN_ERROR = "Unknown error"

public interface HttpScope

@Composable
public fun HttpScope.HttpEffect(block: suspend CoroutineScope.() -> String) {
    val scope = LocalApplicationScope.current
    val response = LocalHttpResponse.current
    val request = LocalHttpRequest.current

    LaunchedEffect(request) {
        try {
            val result: String = block()
            response.setContentType("${ContentType.Application.Json}")
            response.setStatusCode(HttpStatusCode.OK)
            response.write(result)
        } catch (exception: HttpException) {
            response.setStatusCode(exception.code, exception.message)
            response.write(exception.message)
        } catch (exception: Exception) {
            response.setStatusCode(HttpStatusCode.InternalServerError, exception.message)
            response.write(exception.message ?: UNKNOWN_ERROR)
            exception.printStackTrace()
            scope.exitApplication()
        }
    }
}
