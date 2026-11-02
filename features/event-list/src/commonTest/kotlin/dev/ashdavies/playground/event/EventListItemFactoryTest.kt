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
            expected = awaitItem(event).thumbnailImageSeed,
            actual = awaitItem(event).thumbnailImageSeed,
        )
    }

    @Test
    fun `should append name with start year`() = createEvent { event ->
        assertEquals(
            expected = "${event.name} ${event.dateStart.year}",
            actual = awaitItem(event).name,
        )
    }

    @Test
    fun `should use country flag for recognised country `() = createEvent(
        transform = { it.copy(location = "Berlin, Germany") },
    ) { event ->
        assertEquals(
            expected = "🇩🇪 Berlin",
            actual = awaitItem(
                flagStore = mapOf("Germany" to "🇩🇪"),
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
            actual = awaitItem(event).location,
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
            actual = awaitItem(event).date,
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
            actual = awaitItem(event).date,
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
            actual = awaitItem(event).date,
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
            actual = awaitItem(event).date,
        )
    }

    @Test
    fun `should show closed if today after cfp end`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpEnd = "2026-05-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Status.Closed,
            actual = awaitItem(event, LocalDate(2026, 6, 1)).status,
        )
    }

    // TODO Args or delegate function for d/h/m
    @Test
    fun `should show closing if today after cfp end`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpEnd = "2026-06-12" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Status.Closing("12d"),
            actual = awaitItem(event, LocalDate(2026, 6, 1)).status,
        )
    }

    @Test
    fun `should show open if today after cfp start`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpStart = "2026-05-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Status.Open,
            actual = awaitItem(event, LocalDate(2026, 6, 1)).status,
        )
    }

    @Test
    fun `should show opening if today after cfp start`() = createEvent(
        transform = { it.copy(attrs = EventAttrs { cfpStart = "2026-06-01" }) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Status.Opening("Jun 1, 2026"),
            actual = awaitItem(event, LocalDate(2026, 5, 1)).status,
        )
    }

    @Test
    fun `should show not announced if no cfp`() = createEvent(
        transform = { it.copy(attrs = EventAttrs()) },
    ) { event ->
        assertEquals(
            expected = EventListState.Success.Item.Status.NotAnnounced,
            actual = awaitItem(event, LocalDate(2026, 5, 1)).status,
        )
    }
}

private fun awaitItem(
    event: Event = createEvent { it },
    today: LocalDate = LocalDate(2026, 5, 1),
    flagStore: Map<String, String> = emptyMap(),
    isBookmarked: Boolean = false,
): EventListState.Success.Item = EventListItemFactory(
    flagStore = flagStore,
    clock = today.asClock(),
).invoke(
    event = event,
    isBookmarked = isBookmarked,
)

private fun <T> createEvent(transform: (Event) -> Event = { it }, action: (Event) -> T): T {
    return action(transform(Json.upcomingEvents().first()))
}
