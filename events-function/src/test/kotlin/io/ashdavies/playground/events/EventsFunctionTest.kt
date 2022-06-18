package io.ashdavies.playground.events

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class EventsFunctionTest {

    private val request = FakeHttpRequest.build()
    private val response = FakeHttpResponse()
    private val function = EventsFunction()

    @Test
    fun `test assertion succeeds`() = runTest {
        assertEquals(2 + 2, 4)
    }

    @Test
    fun `test assertion fails`() {
        val throwable = assertFails { function.service(request, response) }

        assertTrue {
            throwable is IllegalArgumentException || throwable is IllegalStateException
        }
    }
}
