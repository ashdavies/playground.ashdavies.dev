package io.ashdavies.playground

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.ashdavies.dominion.DominionScreen
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ashdavies.playground.home.HomeScreen as EventsHomeScreen

internal class LauncherPresenterTest {

    private val navigator = FakeNavigator()

    @Test
    fun `should navigate to dominion screen`() = runTest {
        presenterTestOf({ LauncherPresenter(navigator) }) {
            awaitItem().eventSink(LauncherScreen.Event.Dominion)

            assertEquals(navigator.awaitNextScreen(), DominionScreen.Home)
        }
    }

    @Test
    fun `should navigate to events screen`() = runTest {
        presenterTestOf({ LauncherPresenter(navigator) }) {
            awaitItem().eventSink(LauncherScreen.Event.Events)

            assertEquals(navigator.awaitNextScreen(), EventsHomeScreen)
        }
    }

    @Test
    fun `should navigate to ratings screen`() = runTest {
        presenterTestOf({ LauncherPresenter(navigator) }) {
            awaitItem().eventSink(LauncherScreen.Event.Ratings)

            assertEquals(navigator.awaitNextScreen(), RatingsScreen)
        }
    }
}
