package io.ashdavies.playground.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ashdavies.playground.KordulaLaube
import io.ashdavies.playground.Profile

@Preview
@Composable
private fun ProfileScreen(profile: Profile? = null) {
    ProfileScreen(ProfileScreen.State(profile) { })
}

@Preview
@Composable
private fun ProfileScreen() {
    ProfileScreen(KordulaLaube)
}
