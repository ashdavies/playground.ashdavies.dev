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

private const val DAY_IN_SECONDS = 86_400
private const val HOUR_IN_SECONDS = 3_600
private const val MINUTE_IN_SECONDS = 60

private val Event.thumbnailImageSeed: Long
    get() = "$name$location"
        .hashCode()
        .toLong()

@Inject
internal class EventListItemFactory(
    private val flagResolver: CountryFlagResolver,
    private val clock: Clock,
) {

    operator fun invoke(event: Event, isBookmarked: Boolean) = EventListState.Success.Item.Standard(
        thumbnailImageSeed = event.thumbnailImageSeed,
        header = formatDateHeader(event.dateStart),
        uuid = event.uuid,
        name = "${event.name} ${event.dateStart.year}",
        location = event.location.split(", ", limit = 2).let { (city, country) ->
            "${(flagResolver[country] ?: "📍")} $city"
        },
        date = formatDateRange(event.dateStart, event.dateEnd),
        status = status(event.attrs.cfpStart, event.attrs.cfpEnd),
        isBookmarked = isBookmarked,
    )

    private fun status(cfpStart: String?, cfpEnd: String?): EventListState.Success.Item.Standard.Status {
        val cfpStart = cfpStart?.let(LocalDate::parse)
        val cfpEnd = cfpEnd?.let(LocalDate::parse)
        val today = clock.todayIn(TimeZone.UTC)

        return when {
            cfpEnd != null && today > cfpEnd -> EventListState.Success.Item.Standard.Status.Closed

            cfpEnd != null -> EventListState.Success.Item.Standard.Status.Closing(
                remaining = run {
                    val cfpEndInclusive = cfpEnd
                        .plus(1, DateTimeUnit.DAY)
                        .atStartOfDayIn(TimeZone.UTC)

                    val secondsUntilCfp = today
                        .atStartOfDayIn(TimeZone.UTC)
                        .until(cfpEndInclusive, DateTimeUnit.SECOND)

                    when {
                        secondsUntilCfp > DAY_IN_SECONDS * 2 -> "${secondsUntilCfp / DAY_IN_SECONDS}d"
                        secondsUntilCfp > HOUR_IN_SECONDS * 2 -> "${secondsUntilCfp / HOUR_IN_SECONDS}h"
                        secondsUntilCfp > MINUTE_IN_SECONDS * 2 -> "${secondsUntilCfp / MINUTE_IN_SECONDS}m"
                        else -> "${secondsUntilCfp % MINUTE_IN_SECONDS}s"
                    }
                },
            )

            cfpStart != null && today >= cfpStart -> EventListState.Success.Item.Standard.Status.Open

            cfpStart != null -> EventListState.Success.Item.Standard.Status.Opening(formatDateRange(cfpStart))

            else -> EventListState.Success.Item.Standard.Status.NotAnnounced
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
public fun eventListItemFactory(clock: Clock): (Event, Boolean) -> EventListState.Success.Item = EventListItemFactory(
    flagResolver = LocalCountryFlagResolver(),
    clock = clock,
)::invoke
