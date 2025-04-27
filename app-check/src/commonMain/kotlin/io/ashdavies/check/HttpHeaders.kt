package io.ashdavies.check

import io.ktor.http.HttpHeaders

@Suppress("UnusedReceiverParameter")
public val HttpHeaders.AppCheckToken: String get() = "X-Firebase-AppCheck"
