package io.ashdavies.identity

internal actual fun serverClientId(): String = System.getenv("SERVER_CLIENT_ID")
