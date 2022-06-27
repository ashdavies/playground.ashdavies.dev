package io.ashdavies.http

import java.lang.System

internal actual object System {
    actual val properties: Map<Any, Any> = System.getProperties()
    actual val env: Map<String, String> = System.getenv()
}
