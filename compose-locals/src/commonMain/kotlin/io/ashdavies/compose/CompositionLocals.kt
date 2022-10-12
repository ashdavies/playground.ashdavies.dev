package io.ashdavies.compose

public fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
