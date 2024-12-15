package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.profile.ProfileActionButton
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight
import org.jetbrains.compose.resources.stringResource
import playground.conferences_app.generated.resources.Res
import playground.conferences_app.generated.resources.upcoming_events

internal class EventsTopBarTests {

    @Composable
    @PreviewDayNight
    private fun EventsTopBarPreview() {
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
