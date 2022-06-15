package io.ashdavies.playground.events

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

internal class EventsFunctionTest {

    private val request = FakeHttpRequest.build()
    private val response = FakeHttpResponse()
    private val function = EventsFunction()

    @Test(expected = IllegalStateException::class)
    fun test() = runBlocking<Unit> {
        function.service(request, response)
    }
}
