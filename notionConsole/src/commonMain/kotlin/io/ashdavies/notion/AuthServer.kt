package io.ashdavies.notion

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

private const val DEFAULT_HOST = "localhost"
private const val DEFAULT_PATH = "callback"
private const val DEFAULT_PORT = 8080

@ExperimentalTime
public expect class AuthServer(host: String = DEFAULT_HOST, path: String = DEFAULT_PATH, port: Int = DEFAULT_PORT) {
    public suspend fun awaitRedirectUri(duration: Duration = minutes(2)): String
    public fun getRedirectUri(): String
}
