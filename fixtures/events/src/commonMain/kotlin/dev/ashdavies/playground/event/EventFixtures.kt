package dev.ashdavies.playground.event

import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import kotlinx.serialization.json.Json
import playground.fixtures.events.fixtures.UpcomingJson

public object EventFixtures {
    public fun <T : Any> upcoming(transform: (DomainWrapper<DomainEvent>) -> T): List<T> = Json
        .decodeFromString<List<DomainWrapper<DomainEvent>>>(UpcomingJson)
        .map(transform)
}
