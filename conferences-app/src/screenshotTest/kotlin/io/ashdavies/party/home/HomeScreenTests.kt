package io.ashdavies.party.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.profile.ProfileActionButton
import io.ashdavies.party.tooling.MaterialPreviewTheme
import io.ashdavies.party.tooling.PreviewDayNight

internal class HomeScreenTests {

    @Composable
    @PreviewDayNight
    @OptIn(ExperimentalMaterial3Api::class)
    fun HomeTopAppBarPreview() {
        MaterialPreviewTheme {
            HomeTopBar(
                actions = {
                    ProfileActionButton(
                        identityState = IdentityState.Unauthenticated,
                        onClick = { },
                    )
                },
            )
        }
    }

    @Composable
    @PreviewDayNight
    fun HomeBottomSheetPreview() {
        MaterialPreviewTheme {
            HomeBottomBar()
        }
    }
}
