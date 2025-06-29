package io.ashdavies.tally.tooling

import kotlinx.serialization.Serializable

@Serializable
internal data class Location(val city: String, val country: String)

internal fun UnitTestResources.locations(): List<Location> {
    return decodeFromResource("jvmTest", "locations.json")
}
