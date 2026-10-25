package dev.ashdavies.playground.event

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

private val MonthDayFormat = LocalDate.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    day(padding = Padding.NONE)
}

public fun formatDateRange(start: LocalDate, end: LocalDate = start): String = when {
    start == end -> "${start.format(MonthDayFormat)}, ${start.year}"
    start.year != end.year -> "${start.format(MonthDayFormat)}, ${start.year} – ${end.format(MonthDayFormat)}, ${end.year}"
    start.month != end.month -> "${start.format(MonthDayFormat)} – ${end.format(MonthDayFormat)}, ${end.year}"
    else -> "${start.format(MonthDayFormat)}–${end.day}, ${end.year}"
}
