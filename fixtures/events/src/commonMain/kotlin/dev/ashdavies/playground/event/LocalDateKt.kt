package dev.ashdavies.playground.event

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Clock

public fun LocalDate.asClock(timeZone: TimeZone = TimeZone.UTC): Clock = object : Clock {
    override fun now() = atStartOfDayIn(timeZone)
}
