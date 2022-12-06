package io.ashdavies.playground

import app.cash.turbine.test
import io.ashdavies.playground.kotlin.asCloseableFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class CloseableFlowTest {

    @Test
    fun `should execute on jvm main dispatcher`() = runTest {
        val closable = flowOf(1, 2, 3).asCloseableFlow()
        val channel = Channel<Int>(UNLIMITED)
        closable.watch { channel.send(it) }

        channel.receiveAsFlow().test {
            assertEquals(awaitItem(), 1)
            assertEquals(awaitItem(), 2)
            assertEquals(awaitItem(), 3)
        }
    }
}
