package io.ashdavies.notion

import java.lang.System

actual object System {
    actual fun getenv(name: String): String = System.getenv(name)
}
