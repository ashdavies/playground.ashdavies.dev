package dev.ashdavies.playground.event

import dev.ashdavies.http.common.models.EventAttrs
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EventListItemFactoryTest {

    @Test
    fun `should generate identical thumbnailImageSeed`() = createEvent(
        transform = { it.copy(attrs = EventAttrs()) },
    ) { event ->
        assertEquals(
            expected = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).thumbnailImageSeed,
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).thumbnailImageSeed,
        )
    }

    @Test
    fun `should append name with start year`() = createEvent { event ->
        assertEquals(
            expected = "${event.name} ${event.dateStart.year}",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).name,
        )
    }

    @Test
    fun `should use country flag for recognised country `() = createEvent(
        transform = { it.copy(location = "Berlin, Germany") },
    ) { event ->
        assertEquals(
            expected = "🇩🇪 Berlin",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                flagMap = mapOf("Germany" to "🇩🇪"),
                event = event,
            ).location,
        )
    }

    @Test
    fun `should use map pin for unrecognised country`() = createEvent(
        transform = { it.copy(location = "Berlin, Germany") },
    ) { event ->
        assertEquals(
            expected = "📍 Berlin",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).location,
        )
    }

    @Test
    fun `should show single date when dates equal`() = createEvent(
        transform = {
            it.copy(
                dateStart = LocalDate(2026, 8, 1),
                dateEnd = LocalDate(2026, 8, 1),
            )
        },
    ) { event ->
        assertEquals(
            expected = "Aug 1, 2026",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).date,
        )
    }

    @Test
    fun `should show range when years differ`() = createEvent(
        transform = {
            it.copy(
                dateStart = LocalDate(2026, 12, 30),
                dateEnd = LocalDate(2027, 1, 1),
            )
        },
    ) { event ->
        assertEquals(
            expected = "Dec 30, 2026 - Jan 1, 2027",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).date,
        )
    }

    @Test
    fun `should show range when months differ`() = createEvent(
        transform = {
            it.copy(
                dateStart = LocalDate(2026, 9, 30),
                dateEnd = LocalDate(2026, 10, 1),
            )
        },
    ) { event ->
        assertEquals(
            expected = "Sep 30 - Oct 1, 2026",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).date,
        )
    }

    @Test
    fun `should show range when end differs`() = createEvent(
        transform = {
            it.copy(
                dateStart = LocalDate(2026, 7, 15),
                dateEnd = LocalDate(2026, 7, 16),
            )
        },
    ) { event ->
        assertEquals(
            expected = "Jul 15 - 16, 2026",
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
            ).date,
        )
    }

    @Test
    fun `should show closed if today after cfp end`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpEnd = "2026-05-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Standard.Status.Closed,
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
                today = LocalDate(2026, 6, 1),
            ).status,
        )
    }

    @Test
    fun `should show closing if today after cfp end`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpEnd = "2026-06-12" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Standard.Status.Closing("12d"),
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
                today = LocalDate(2026, 6, 1),
            ).status,
        )
    }

    @Test
    fun `should show open if today after cfp start`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpStart = "2026-05-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Standard.Status.Open,
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
                today = LocalDate(2026, 6, 1),
            ).status,
        )
    }

    @Test
    fun `should show opening if today after cfp start`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpStart = "2026-06-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Standard.Status.Opening("Jun 1, 2026"),
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
                today = LocalDate(2026, 5, 1),
            ).status,
        )
    }

    @Test
    fun `should show not announced if no cfp`() = createEvent(
        transform = { it.copy(attrs = EventAttrs()) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Standard.Status.NotAnnounced,
            actual = awaitItem<EventListState.Success.Item.Standard>(
                event = event,
                today = LocalDate(2026, 5, 1),
            ).status,
        )
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T : EventListState.Success.Item> awaitItem(
    event: Event = createEvent { it },
    today: LocalDate = LocalDate(2026, 5, 1),
    flagMap: Map<String, String> = emptyMap(),
    isBookmarked: Boolean = false,
): T = EventListItemFactory(
    flagResolver = LocalCountryFlagResolver(flagMap),
    clock = today.asClock(),
).invoke(
    event = event,
    isBookmarked = isBookmarked,
) as T

private fun <T> createEvent(transform: (Event) -> Event = { it }, action: (Event) -> T): T {
    return action(transform(Json.upcomingEvents().first()))
}
