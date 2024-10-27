package io.ashdavies.party.events

import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlinx.datetime.format.MonthNames

private val MonthNamesList = listOf(
    "Jan", // Res.string.january,
    "Feb", // Res.string.february,
    "Mar", // Res.string.march,
    "Apr", // Res.string.april,
    "May", // Res.string.may,
    "Jun", // Res.string.june,
    "Jul", // Res.string.july,
    "Aug", // Res.string.august,
    "Sep", // Res.string.september,
    "Oct", // Res.string.october,
    "Nov", // Res.string.november,
    "Dec", // Res.string.december,
)

internal fun DateTimeFormatBuilder.WithDate.monthName() {
    monthName(MonthNames(MonthNamesList))
}
