package dev.ashdavies.check

import io.ktor.http.HttpHeaders

@Suppress("UnusedReceiverParameter")
public val HttpHeaders.XFirebaseAppCheck: String get() = "X-Firebase-AppCheck"
