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
    fun `should execute on swing dispatcher`() = runTest {
        val flow = flowOf(1, 2, 3, 4, 5)
        val closable = flow.asCloseableFlow()

        val channel = Channel<Int>(UNLIMITED)
        closable.watch { channel.send(it) }

        channel.receiveAsFlow().test {
            assertEquals(awaitItem(), 1)
            assertEquals(awaitItem(), 2)
            assertEquals(awaitItem(), 3)
            assertEquals(awaitItem(), 4)
            assertEquals(awaitItem(), 5)
        }
    }
}
