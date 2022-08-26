package io.ashdavies.playground.events

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class EventsFunctionTest {

    @Test
    fun `test assertion succeeds`() = runTest {
        assertEquals(2 + 2, 4)
    }
}
