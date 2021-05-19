package io.ashdavies.playground.database

import io.ashdavies.playground.conferences.ConferenceId
import kotlinx.datetime.LocalDate

class DatabaseFactory(private val driverFactory: DriverFactory) {

    suspend fun create() = PlaygroundDatabase(
        driver = driverFactory.create(),
        conferenceAdapter = Conference.Adapter(
            idAdapter = ConferenceId.Adapter,
            dateStartAdapter = LocalDate.Adapter,
            dateEndAdapter = LocalDate.Adapter,
            cfpStartAdapter = LocalDate.Adapter,
            cfpEndAdapter = LocalDate.Adapter,
        ),
    )
}
