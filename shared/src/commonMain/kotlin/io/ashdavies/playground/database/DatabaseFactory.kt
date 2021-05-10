package io.ashdavies.playground.database

import io.ashdavies.playground.conferences.CfpId
import io.ashdavies.playground.conferences.ConferenceId
import kotlinx.datetime.LocalDate

class DatabaseFactory(private val driverFactory: DriverFactory) {

    fun create() = PlaygroundDatabase(
        driver = driverFactory.create(),
        conferenceAdapter = Conference.Adapter(
            idAdapter = ConferenceId.Adapter,
            dateStartAdapter = LocalDate.Adapter,
            dateEndAdapter = LocalDate.Adapter,
            cfpIdAdapter = CfpId.Adapter,
        ),
        cfpAdapter = Cfp.Adapter(
            idAdapter = CfpId.Adapter,
            startAdapter = LocalDate.Adapter,
            endAdapter = LocalDate.Adapter,
        ),
    )
}
