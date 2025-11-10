package dev.ashdavies.cloud

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.http.common.models.ApiConference

public fun AsgConference.toApiConference(id: String): ApiConference = ApiConference(
    id = id,
    name = name,
    website = website,
    location = location,
    imageUrl = null,
    dateStart = dateStart,
    dateEnd = dateEnd,
    status = status,
    online = online ?: false,
    cfp = cfp?.let {
        ApiConference.Cfp(
            start = it.start,
            end = it.end,
            site = it.site,
        )
    },
)
