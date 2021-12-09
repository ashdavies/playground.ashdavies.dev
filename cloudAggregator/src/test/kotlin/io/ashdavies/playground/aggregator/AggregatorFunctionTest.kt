package io.ashdavies.playground.aggregator

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

internal class AggregatorFunctionTest {

    private val function = AggregatorFunction()

    @Test(expected = IllegalStateException::class)
    fun test() = runBlocking<Unit> {
        function.firebaseApp
        TODO("Not yet implemented")
    }
}
