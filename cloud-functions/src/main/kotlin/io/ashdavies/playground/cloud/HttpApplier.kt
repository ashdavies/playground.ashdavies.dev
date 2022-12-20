package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

private object HttpApplierScope : HttpScope

internal fun HttpApplier(config: HttpConfig, call: HttpCall): HttpApplier {
    return HttpApplier { request: HttpRequest, response: HttpResponse ->
        val method = HttpMethod.parse(request.method)
        val contentType = request.contentType
            .map { ContentType.parse(it) }
            .orElse(null)

        when {
            method != config.allow -> {
                response.appendHeader(HttpHeaders.Allow, config.allow.value)
                response.setStatusCode(HttpStatusCode.MethodNotAllowed)
            }

            method == HttpMethod.Post && request.contentLength == 0L -> {
                response.setStatusCode(HttpStatusCode.BadRequest)
            }

            contentType != config.accept -> {
                response.appendHeader(HttpHeaders.Accept, "${config.accept}")
                response.setStatusCode(HttpStatusCode.UnsupportedMediaType)
            }

            else -> try {
                HttpApplierScope.call(request, response)
            } catch (throwable: Throwable) {
                response.setStatusCode(HttpStatusCode.InternalServerError, throwable.message)
                response.writer.write("${throwable.message}")
                throwable.printStackTrace()
            }
        }
    }
}
