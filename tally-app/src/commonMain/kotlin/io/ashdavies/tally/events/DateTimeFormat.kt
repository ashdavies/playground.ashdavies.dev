package io.ashdavies.tally.events

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames

private val EnglishMonthNames = LocalDate.Format { monthName(MonthNames.ENGLISH_ABBREVIATED) }
private val Day = LocalDate.Format { day() }

internal fun LocalDate.monthAsString(): String = format(EnglishMonthNames)

internal fun LocalDate.dayAsString(): String = format(Day)
