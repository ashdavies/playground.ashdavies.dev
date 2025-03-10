package io.ashdavies.party.events

import app.cash.paparazzi.Paparazzi
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.profile.ProfileActionButton
import io.ashdavies.party.tooling.MaterialPreviewTheme
import org.jetbrains.compose.resources.stringResource
import org.junit.Rule
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.upcoming_events
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
