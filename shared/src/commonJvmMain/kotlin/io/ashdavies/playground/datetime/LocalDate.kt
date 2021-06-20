package io.ashdavies.playground.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.util.Calendar
import java.util.Calendar.getInstance

fun LocalDate.toCalendar(): Calendar = getInstance().apply {
    set(Calendar.MONTH, month.number)
    set(Calendar.YEAR, year)
}