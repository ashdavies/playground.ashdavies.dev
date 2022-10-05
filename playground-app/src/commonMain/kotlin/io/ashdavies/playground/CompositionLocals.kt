package io.ashdavies.playground

public fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
