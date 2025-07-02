package io.ashdavies.tally.events

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

internal fun daysUntilCfpEnd(cfpEnd: LocalDate): Int {
    return Today.daysUntil(cfpEnd)
}
