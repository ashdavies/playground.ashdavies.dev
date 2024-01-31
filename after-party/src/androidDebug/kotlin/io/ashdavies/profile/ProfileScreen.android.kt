package io.ashdavies.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun EmptyProfileScreenPreview() {
    ProfileScreen(ProfileScreen.State(null) { })
}

@Preview
@Composable
internal fun ProfileScreenPreview() {
    ProfileScreen(ProfileScreen.State(KordulaLaube) { })
}
