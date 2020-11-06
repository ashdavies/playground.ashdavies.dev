package io.ashdavies.playground.ktx

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.util.Calendar
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

internal fun LocalDate.toCalendar(): Calendar =
    Calendar
        .getInstance()
        .apply {
            set(MONTH, month.number)
            set(YEAR, year)
        }
