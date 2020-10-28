package io.ashdavies.playground.conferences

import io.ashdavies.playground.network.Conference
import java.util.Calendar
import java.util.Locale
import java.util.Locale.getDefault

internal class ConferencesNameParser : (List<Conference>) -> List<Conference> {

    override fun invoke(it: List<Conference>): List<Conference> =
        it.map { it.enrichFromName() }

    private fun Conference.enrichFromName(): Conference {
        val splitName: List<String> =
            name
                .substringBefore(".md")
                .split("-", "_", ".")

        val parsedYear: Int =
            splitName
                .last()
                .toIntOrNull()
                ?: return copy(
                    name = splitName.joinToCapitalizedString()
                )

        val truncatedName: String =
            splitName
                .subList(0, splitName.size - 2)
                .joinToCapitalizedString()

        return copy(
            dateStart = startOfYear(parsedYear),
            name = truncatedName,
        )
    }
}

private fun startOfYear(year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.clear()

    calendar.set(Calendar.YEAR, year)
    return calendar.timeInMillis
}

private fun Iterable<String>.joinToCapitalizedString(
    separator: String = " ",
    locale: Locale = getDefault()
): String = joinToString(separator) {
    it.capitalize(locale)
}
