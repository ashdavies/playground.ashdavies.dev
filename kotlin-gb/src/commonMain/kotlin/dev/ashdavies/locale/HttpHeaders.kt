package dev.ashdavies.locale

import io.ktor.http.HttpHeaders

public val HttpHeaders.Authorisation: String
    get() = Authorization
