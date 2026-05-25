package dev.ashdavies.playground.event

import dev.ashdavies.asg.AsgConference

internal fun AsgConference.toEvent(id: Long) = Event(
    id = id,
    name = name,
    website = website,
    location = location,
    imageUrl = null,
    status = status,
    online = online,
    dateStart = dateStart,
    dateEnd = dateEnd,
    cfpStart = cfp?.start,
    cfpEnd = cfp?.end,
    cfpSite = cfp?.site,
)
