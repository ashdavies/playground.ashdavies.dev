package dev.ashdavies.cloud

import dev.ashdavies.playground.event.asgUpcomingConferences
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AsgConferenceUuidTest {

    private val conferences = Json.asgUpcomingConferences { it }

    @Test
    fun `should generate unique uuid`() {
        assertEquals(
            expected = conferences.size,
            actual = conferences
                .map { it.uuid() }
                .distinct()
                .size,
        )
    }

    @Test
    fun `should maintain start date order`() {
        assertEquals(
            expected = conferences
                .map { it.dateStart }
                .sorted(),
            actual = conferences
                .shuffled()
                .sortedBy { "${it.uuid()}" }
                .map { it.dateStart },
        )
    }
}
