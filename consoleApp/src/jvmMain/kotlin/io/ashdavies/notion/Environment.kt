package io.ashdavies.notion

actual object Environment {
    actual operator fun get(name: String): String = System.getenv(name)
}
