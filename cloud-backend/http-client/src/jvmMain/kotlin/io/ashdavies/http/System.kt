package io.ashdavies.http

import java.lang.System

public actual object System : Environment {
    override val properties: Map<Any, Any> = System.getProperties()
    override val env: Map<String, String> = System.getenv()
}
