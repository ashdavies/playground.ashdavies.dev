package io.ashdavies.notion

import java.lang.System

public actual object System {
    public actual fun getenv(name: String): String = requireNotNull(System.getenv(name))
}
