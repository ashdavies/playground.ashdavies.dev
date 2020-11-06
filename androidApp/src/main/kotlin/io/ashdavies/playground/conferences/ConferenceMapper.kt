package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference

internal class ConferenceMapper : (ConferenceYaml) -> Conference {

    override fun invoke(input: ConferenceYaml): Conference =
        Conference(
            name = input.name,
            website = input.website,
            location = input.location,
            status = input.status,
            dateStart = input.dateStart,
            dateEnd = input.dateEnd,
            cfpStart = input.cfp?.start,
            cfpEnd = input.cfp?.end,
            cfpSite = input.cfp?.site,
            online = input.online,
        )
}
