package io.ashdavies.party

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.identity.IdentityState

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AfterPartyTopBarPreview() {
    AfterPartyPreviewTheme {
        AfterPartyTopBar(
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
internal fun AfterPartyBottomSheetPreview() {
    AfterPartyPreviewTheme {
        AfterPartyBottomBar(AfterPartyScreen)
    }
}

@Composable
private fun AfterPartyPreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        content()
    }
}
