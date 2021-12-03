package io.ashdavies.notion

import io.ashdavies.playground.DatabaseFactory

interface UuidRegistrar {
    suspend fun lookup(short: String): UuidValue
    suspend fun register(uuid: UuidValue)
}

fun UuidRegistrar(): UuidRegistrar {
    val database: UuidRegistry = DatabaseFactory.create(
        block = UuidRegistry.Companion::invoke,
        schema = UuidRegistry.Schema,
    )

    return SqlUuidRegistrar(database.uuidRegistryQueries)
}

suspend fun UuidRegistrar.register(name: String) {
    register(UuidValue.fromString(name))
}
