package io.ashdavies.http

import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

public fun server(configure: (String, String) -> Unit) {
    configure(HttpHeaders.Server, Software.gitCommit)
}

internal fun HttpMessageBuilder.server() {
    server(::header)
}
