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

private val FullMonthDayFormat = LocalDate.Format {
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year()
}

public fun formatDateHeader(start: LocalDate): String {
    return start.format(FullMonthDayFormat)
}

public fun formatDateRange(start: LocalDate, end: LocalDate = start): String {
    val startDate = start.format(MonthDayFormat)
    val endDate = end.format(MonthDayFormat)

    return when {
        start == end -> "$startDate, ${start.year}"
        start.year != end.year -> "$startDate, ${start.year} - $endDate, ${end.year}"
        start.month != end.month -> "$startDate - $endDate, ${end.year}"
        else -> "$startDate - ${end.day}, ${end.year}"
    }
}
