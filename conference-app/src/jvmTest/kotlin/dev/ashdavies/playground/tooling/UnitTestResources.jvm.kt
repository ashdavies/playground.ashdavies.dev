package dev.ashdavies.playground.tooling

import dev.ashdavies.playground.tooling.UnitTestResources
import kotlinx.serialization.Serializable

internal fun UnitTestResources.locations(): List<Location> {
    return decodeFromResource("jvmTest", "locations.json")
}

@Serializable
internal data class Location(val city: String, val country: String)
