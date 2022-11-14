package io.ashdavies.playground.aggregator

import io.ashdavies.cloud.startServer
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AggregatorFunctionTest {

    @Test
    @Ignore
    fun `should aggregate events from github`() = startServer<AggregatorFunction> { client ->
        assertEquals(HttpStatusCode.OK, client.get { }.status)
    }
}
