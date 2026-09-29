package dev.ashdavies.playground.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

private val EnglishMonthNamesFormat = LocalDate.Format { monthName(MonthNames.ENGLISH_ABBREVIATED) }
private val DayFormat = LocalDate.Format { day() }

private const val HYPHEN = "-"

public data class DateRangeBadgeState(
    val dateStart: LocalDate,
    val dateEnd: LocalDate,
)

@Composable
public fun DateRangeBadge(
    state: DateRangeBadgeState,
    color: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier,
) {
    BadgeContainer(modifier, color) {
        val startMonth = state.dateStart.format(EnglishMonthNamesFormat)
        val endMonth = state.dateEnd.format(EnglishMonthNamesFormat)

        val startDay = state.dateStart.format(DayFormat)
        val endDay = state.dateEnd.format(DayFormat)

        when {
            startMonth != endMonth -> Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(startMonth, style = MaterialTheme.typography.labelSmall)
                    Text(startDay, style = MaterialTheme.typography.labelLarge)
                }

                Text(
                    text = HYPHEN,
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    style = MaterialTheme.typography.labelLarge,
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(endMonth, style = MaterialTheme.typography.labelSmall)
                    Text(endDay, style = MaterialTheme.typography.labelLarge)
                }
            }

            startDay != endDay -> {
                Text(startMonth, style = MaterialTheme.typography.labelSmall)
                Text("$startDay $HYPHEN $endDay", style = MaterialTheme.typography.labelLarge)
            }

            else -> {
                Text(startMonth, style = MaterialTheme.typography.labelSmall)
                Text(startDay, style = MaterialTheme.typography.labelLarge)
            }
        }

        val currentYear = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .year

        if (state.dateStart.year != currentYear) {
            Text(
                text = state.dateStart.format(LocalDate.Format { year() }),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
