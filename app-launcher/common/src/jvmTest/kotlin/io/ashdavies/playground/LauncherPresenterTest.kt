package io.ashdavies.playground

import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.ashdavies.party.afterPartyScreen
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LauncherPresenterTest {

    private val navigator = FakeNavigator(LauncherScreen)

    @Test
    fun `should navigate to after party screen`() = runTest {
        presenterTestOf({ LauncherPresenter(navigator) }) {
            awaitItem().eventSink(NavEvent.GoTo(afterPartyScreen()))

            assertEquals(navigator.awaitNextScreen(), afterPartyScreen())
        }
    }
}
