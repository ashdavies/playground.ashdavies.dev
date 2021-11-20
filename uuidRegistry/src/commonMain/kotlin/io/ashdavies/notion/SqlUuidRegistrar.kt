package io.ashdavies.notion

internal class SqlUuidRegistrar(private val registry: UuidRegistryQueries) : UuidRegistrar {

    override suspend fun lookup(short: String): UuidValue =
        registry
            .select(short)
            .executeAsOne()
            .let(UuidValue::fromString)

    override suspend fun register(uuid: UuidValue): Unit =
        registry.insert(uuid.toString(), uuid.short)
}
