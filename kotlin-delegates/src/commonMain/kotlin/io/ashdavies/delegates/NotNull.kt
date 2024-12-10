package io.ashdavies.delegates

import kotlin.properties.ReadOnlyProperty

public fun <T> notNull(block: () -> T?): ReadOnlyProperty<Any?, T> {
    return ReadOnlyProperty<Any?, T> { _, property ->
        requireNotNull(block()) { "Missing property ${property.name}" }
    }
}
