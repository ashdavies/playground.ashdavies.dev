package io.ashdavies.playground.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.navigation.NavHost
import androidx.compose.navigation.composable
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import io.ashdavies.playground.R
import io.ashdavies.playground.conferences.ConferencesScreen
import io.ashdavies.playground.navigation.ScreenDestination.Conferences

@Preview
@Composable
fun MainScreen() {
    MaterialTheme {
        Column {
            TopAppBar(
                title = { Text(stringResource(R.string.application)) },
                navigationIcon = {
                    IconButton(onClick = { /* do something here */ }) {
                        Image(asset = vectorResource(R.drawable.ic_baseline_menu_24))
                    }
                },

                actions = {
                    IconButton(onClick = { /* do something here */ }) {
                        Image(asset = vectorResource(R.drawable.ic_baseline_search_24))
                    }
                },
            )

            NavHost(startDestination = Conferences) {
                composable(Conferences) { ConferencesScreen() }
            }
        }
    }
}
