package io.ashdavies.tally.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.ashdavies.tally.material.padding
import io.ashdavies.tally.material.spacing
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val HYPHEN = "-"

@Composable
internal fun EventDateLabel(
    dateStart: LocalDate,
    dateEnd: LocalDate,
    modifier: Modifier = Modifier,
) {
    Surface(modifier.clip(MaterialTheme.shapes.small)) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val startMonth = dateStart.monthAsString()
            val endMonth = dateEnd.monthAsString()

            val startDay = dateStart.dayAsString()
            val endDay = dateEnd.dayAsString()

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

            @OptIn(ExperimentalTime::class)
            val currentYear = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .year

            if (dateStart.year != currentYear) {
                Text(
                    text = dateStart.format(LocalDate.Format { year() }),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
