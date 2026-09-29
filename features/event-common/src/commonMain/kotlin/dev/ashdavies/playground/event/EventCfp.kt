package dev.ashdavies.playground.event

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

public fun daysUntilCfpEnd(cfpEnd: LocalDate): Int {
    return Today.daysUntil(cfpEnd)
}
