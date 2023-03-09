package io.ashdavies.http

import io.ktor.http.HttpHeaders

@Suppress("UnusedReceiverParameter")
public val HttpHeaders.AppCheckToken: String get() = "X-Firebase-AppCheck"
