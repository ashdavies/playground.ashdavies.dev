package io.ashdavies.party.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.identity.IdentityState
import io.ashdavies.party.profile.ProfileActionButton

@Preview
@Composable
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

@Preview
@Composable
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
