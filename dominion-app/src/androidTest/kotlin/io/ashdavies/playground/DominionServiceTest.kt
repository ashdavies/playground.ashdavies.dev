package io.ashdavies.playground

import app.cash.molecule.testing.testMolecule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class DominionServiceTest {

    @Test
    fun `should create dominion service`() {
        testMolecule(
            body = { 4 }
        ) {
            assertEquals(4, awaitItem())
        }
    }
}
