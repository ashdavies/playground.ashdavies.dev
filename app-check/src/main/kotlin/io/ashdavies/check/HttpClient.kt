package io.ashdavies.check

import com.auth0.jwt.exceptions.JWTVerificationException
import io.ashdavies.http.catch
import io.ashdavies.playground.cloud.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

internal val HttpStatusCode.isError: Boolean
    get() = value in (400 until 600)

internal suspend inline fun <reified T> HttpClient.post(urlString: String, body: Any?): T = runCatching {
    val response: HttpResponse = post(urlString) { setBody(body) }
    if (response.status.isError) throw HttpException(response)
    return@runCatching response.body<T>()
} catch { it: JWTVerificationException ->
    throw HttpException.InvalidArgument(requireNotNull(it.message), it)
}

internal suspend fun HttpException(response: HttpResponse): HttpException {
    return HttpException(response.status.value, response.body())
}
