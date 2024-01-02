package io.ashdavies.playground

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.ashdavies.party.AfterPartyScreen
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LauncherPresenterTest {

    private val navigator = FakeNavigator()

    @Test
    fun `should navigate to after party screen`() = runTest {
        presenterTestOf({ LauncherPresenter(navigator) }) {
            awaitItem().eventSink(LauncherScreen.Event.AfterParty)

            assertEquals(navigator.awaitNextScreen(), AfterPartyScreen)
        }
    }
}
