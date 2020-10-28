package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import java.util.Calendar
import java.util.Locale.getDefault

internal class ConferencesNameParser : (List<Conference>) -> List<Conference> {

    override fun invoke(it: List<Conference>): List<Conference> =
        it.map { it.enrichFromName() }

    private fun Conference.enrichFromName(): Conference {
        val split: List<String> = name
            .substringBefore(".md")
            .split("-", "_", ".")

        val year: Long = split
            .last()
            .toIntOrNull()
            ?.let(::startOfYear)
            ?: 0

        val name = split
            .subList(0, split.size - 2)
            .joinToString(" ") {
                it.capitalize(getDefault())
            }

        return copy(
            dateStart = year,
            name = name,
        )
    }

    private fun startOfYear(year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.clear()

        calendar.set(Calendar.YEAR, year)
        return calendar.timeInMillis
    }
}
