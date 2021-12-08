package io.ashdavies.notion

expect object Environment {
    operator fun get(name: String): String
}
