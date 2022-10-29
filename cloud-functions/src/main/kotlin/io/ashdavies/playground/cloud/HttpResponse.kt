package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpResponse
import io.ktor.http.HttpStatusCode

internal fun HttpResponse.setStatusCode(code: HttpStatusCode) = setStatusCode(code.value)

internal fun HttpResponse.setStatusCode(code: HttpStatusCode, message: String?) = setStatusCode(code.value, message)

internal fun HttpResponse.write(content: String) = writer.write(content)
