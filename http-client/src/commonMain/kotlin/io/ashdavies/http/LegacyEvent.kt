package io.ashdavies.http

import io.ashdavies.generated.models.Event
import io.ashdavies.playground.Event as LegacyEvent

public fun LegacyEvent(event: Event): LegacyEvent = LegacyEvent(
    dateStart = event.dateStart,
    cfpStart = event.cfp?.start,
    cfpSite = event.cfp?.site,
    location = event.location,
    cfpEnd = event.cfp?.end,
    dateEnd = event.dateEnd,
    website = event.website,
    online = event.online,
    status = event.status,
    name = event.name,
    id = event.id,
)
