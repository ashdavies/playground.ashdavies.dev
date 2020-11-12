package io.ashdavies.playground.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate

class DatabaseFactory(private val driverFactory: DriverFactory) {

    fun create(): PlaygroundDatabase =
        PlaygroundDatabase(
            conferenceAdapter = Conference.Adapter(
                dateStartAdapter = LocalDateAdapter,
                dateEndAdapter = LocalDateAdapter,
                cfpStartAdapter = LocalDateAdapter,
                cfpEndAdapter = LocalDateAdapter,
            ),
            driver = driverFactory.create(),
        )

    private object LocalDateAdapter : ColumnAdapter<LocalDate, String> {

        override fun encode(value: LocalDate): String =
            value.toString()

        override fun decode(databaseValue: String): LocalDate =
            LocalDate.parse(databaseValue)
    }
}
