package dev.ashdavies.cloud

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.http.common.models.ApiConference

public fun interface ApiConferenceFactory {
    public operator fun invoke(value: AsgConference): ApiConference
}

public fun ApiConferenceFactory(identifier: Identifier<AsgConference>): ApiConferenceFactory {
    return object : ApiConferenceFactory {
        override fun invoke(value: AsgConference) = ApiConference(
            id = identifier(value),
            name = value.name,
            website = value.website,
            location = value.location,
            imageUrl = null,
            dateStart = value.dateStart,
            dateEnd = value.dateEnd,
            status = value.status,
            online = value.online ?: false,
            cfp = value.cfp?.let {
                ApiConference.Cfp(
                    start = it.start,
                    end = it.end,
                    site = it.site,
                )
            },
        )
    }
}
