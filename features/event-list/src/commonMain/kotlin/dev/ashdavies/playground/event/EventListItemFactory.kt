package dev.ashdavies.playground.event

import androidx.annotation.VisibleForTesting
import dev.zacsweers.metro.Inject
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import kotlin.time.Clock

private val Event.thumbnailImageSeed: Long
    get() = "$name$location"
        .hashCode()
        .toLong()

@Inject
internal class EventListItemFactory(
    @CountryFlagStore private val flagStore: Map<String, String>,
    private val clock: Clock,
) {

    operator fun invoke(event: Event, isBookmarked: Boolean) = EventListState.Success.Item(
        thumbnailImageSeed = event.thumbnailImageSeed,
        uuid = event.uuid,
        name = "${event.name} ${event.dateStart.year}",
        location = event.location.split(", ", limit = 2).let { (city, country) ->
            "${(flagStore[country] ?: "📍")} $city"
        },
        date = formatDateRange(event.dateStart, event.dateEnd),
        status = status(event.attrs.cfpStart, event.attrs.cfpEnd),
        isBookmarked = isBookmarked,
    )

    private fun status(cfpStart: String?, cfpEnd: String?): EventListState.Success.Item.Status {
        val cfpStart = cfpStart?.let(LocalDate::parse)
        val cfpEnd = cfpEnd?.let(LocalDate::parse)
        val today = clock.todayIn(TimeZone.UTC)

        return when {
            cfpEnd != null && today > cfpEnd -> EventListState.Success.Item.Status.Closed

            cfpEnd != null -> EventListState.Success.Item.Status.Closing(
                remaining = run {
                    val cfpEndInclusive = cfpEnd
                        .plus(1, DateTimeUnit.DAY)
                        .atStartOfDayIn(TimeZone.UTC)

                    val secondsUntilCfp = today
                        .atStartOfDayIn(TimeZone.UTC)
                        .until(cfpEndInclusive, DateTimeUnit.SECOND)

                    when {
                        secondsUntilCfp > 172_800 -> "${secondsUntilCfp / 86_400}d"
                        secondsUntilCfp > 7_200 -> "${secondsUntilCfp / 3_600}h"
                        secondsUntilCfp > 60 -> "${secondsUntilCfp / 60}m"
                        else -> "${secondsUntilCfp % 60}s"
                    }
                },
            )

            cfpStart != null && today >= cfpStart -> EventListState.Success.Item.Status.Open

            cfpStart != null -> EventListState.Success.Item.Status.Opening(formatDateRange(cfpStart))

            else -> EventListState.Success.Item.Status.NotAnnounced
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
public fun eventListItemFactory(clock: Clock): (Event, Boolean) -> EventListState.Success.Item = EventListItemFactory(
    flagStore = CountryFlagsBindings.provideCountryFlagStore(),
    clock = clock,
)::invoke
