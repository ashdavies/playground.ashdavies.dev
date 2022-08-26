package io.ashdavies.playground.events

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EventsFunctionTest {

    private val request = FakeHttpRequest.build()
    private val response = FakeHttpResponse()
    private val function = EventsFunction()

    @Test
    fun test() = runTest {
        // function.service(request, response)
        assertEquals(2 + 2, 4)
    }
}
