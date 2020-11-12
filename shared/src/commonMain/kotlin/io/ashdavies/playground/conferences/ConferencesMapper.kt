package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.network.FirestoreConference

class ConferencesMapper {

    operator fun invoke(value: FirestoreConference): Conference =
        Conference(
            name = value.name.value,
            website = value.website.value,
            location = value.location.value,
            status = value.status?.value,
            dateStart = value.dateStart.value,
            dateEnd = value.dateEnd.value,
            cfpStart = null,//value.cfpStart?.value,
            cfpEnd = null,//value.cfpEnd?.value,
            cfpSite = null,//value.cfpSite?.value,
            online = value.online?.value,
        )
}
