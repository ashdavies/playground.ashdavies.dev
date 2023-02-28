package io.ashdavies.http

import io.ktor.http.HttpHeaders

public fun server(configure: (String, String) -> Unit) {
    configure(HttpHeaders.Server, Software.gitCommit)
}
