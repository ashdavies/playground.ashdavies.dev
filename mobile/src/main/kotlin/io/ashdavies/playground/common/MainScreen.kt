package io.ashdavies.playground.common

import androidx.compose.foundation.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.R

@Preview
@Composable
fun MainActivityScreen() {
    TopAppBar {
        Text(text = stringResource(R.string.application))
    }
}
