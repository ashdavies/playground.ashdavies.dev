package io.ashdavies.lanyard.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.identity.IdentityState
import io.ashdavies.lanyard.profile.ProfileActionButton
import io.ashdavies.lanyard.tooling.MaterialPreviewTheme
import org.jetbrains.compose.resources.stringResource
import org.junit.Rule
import playground.lanyard_app.generated.resources.Res
import playground.lanyard_app.generated.resources.upcoming_events
import kotlin.test.Test

internal class EventsTopBarTests {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun compose() {
        paparazzi.snapshot {
            MaterialPreviewTheme {
                EventsTopBar(
                    title = stringResource(Res.string.upcoming_events),
                    actions = {
                        ProfileActionButton(
                            identityState = IdentityState.Unauthenticated,
                            onClick = { },
                        )
                    },
                )
            }
        }
    }
}
