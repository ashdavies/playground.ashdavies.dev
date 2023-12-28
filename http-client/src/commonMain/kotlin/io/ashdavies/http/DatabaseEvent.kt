package io.ashdavies.http

import io.ashdavies.http.common.models.Event as ApiEvent
import io.ashdavies.playground.Event as DatabaseEvent

public fun DatabaseEvent(event: ApiEvent): DatabaseEvent = DatabaseEvent(
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
