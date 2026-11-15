package dev.ashdavies.http.common.models

import dev.ashdavies.playground.event.Event as DatabaseEvent

public fun DomainWrapper<DomainEvent>.toDatabaseEvent(): DatabaseEvent = DatabaseEvent(
    attrs = EventAttrs(attrs.toMutableMap()),
    uuid = value.uuid,
    name = value.name,
    website = value.website,
    location = value.location,
    dateStart = value.dateStart,
    dateEnd = value.dateEnd,
)
