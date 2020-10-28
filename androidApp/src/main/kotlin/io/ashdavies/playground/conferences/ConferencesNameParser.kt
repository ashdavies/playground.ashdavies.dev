package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import java.util.Calendar
import java.util.Calendar.YEAR
import java.util.Locale.getDefault

internal class ConferencesNameParser : (List<Conference>) -> List<Conference> {

    private val startOfYear: Long =
        Calendar
            .getInstance()
            .get(YEAR)
            .let { startOfYear(it - 1) }

    override fun invoke(it: List<Conference>): List<Conference> =
        it
            .map { it.enrichFromName() }
            .filter { it.dateStart > startOfYear }
            .sortedBy { it.dateStart }

    private fun Conference.enrichFromName(): Conference {
        val splitName: List<String> =
            name
                .substringBefore(".md")
                .split("-", "_", ".")

        val joinedName: String =
            splitName
                .joinToString(" ") {
                    it.capitalize(getDefault())
                }

        val parsedYear: Int =
            splitName
                .last()
                .toIntOrNull()
                ?: return copy(name = joinedName)

        return copy(
            dateStart = startOfYear(parsedYear),
            name = joinedName,
        )
    }
}

private fun startOfYear(year: Int): Long =
    Calendar
        .getInstance()
        .apply { clear(); set(YEAR, year) }
        .timeInMillis
