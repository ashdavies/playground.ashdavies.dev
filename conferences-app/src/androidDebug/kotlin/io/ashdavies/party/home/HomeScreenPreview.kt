package io.ashdavies.party.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.profile.ProfileActionButton
import io.ashdavies.party.tooling.DayNightPreview

@Composable
@DayNightPreview
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeTopAppBarPreview() {
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
@DayNightPreview
internal fun HomeBottomSheetPreview() {
    MaterialPreviewTheme {
        HomeBottomBar(HomeScreen)
    }
}

@Composable
private fun MaterialPreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        content()
    }
}
