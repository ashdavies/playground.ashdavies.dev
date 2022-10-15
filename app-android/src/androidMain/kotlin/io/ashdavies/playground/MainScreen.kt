package io.ashdavies.playground

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
@ExperimentalMaterial3Api
internal fun MainScreen() {
    MaterialTheme {
        Scaffold(topBar = { MainTopAppBar() }) {
            Text("Hello World")
        }
    }
}

@Composable
private fun MainTopAppBar() {
    SmallTopAppBar(title = {
        Text(stringResource(R.string.application))
    })
}
