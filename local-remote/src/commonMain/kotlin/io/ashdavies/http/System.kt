package io.ashdavies.http

internal expect object System {
    val properties: Map<Any, Any>
    val env: Map<String, String>
}
