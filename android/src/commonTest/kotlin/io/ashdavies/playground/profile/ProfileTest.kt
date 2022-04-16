package io.ashdavies.playground.profile

import io.ashdavies.playground.profile.something.Something
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ProfileTest {

    @Test
    fun testDeferredProperty() {
        val deferred = Something.deferred { 3 }

        assertEquals(12, deferred())
    }
}
