package io.ashdavies.party.events

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

private val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

internal fun daysUntilCfpEnd(cfpEnd: LocalDate): Int {
    return Today.daysUntil(cfpEnd)
}
