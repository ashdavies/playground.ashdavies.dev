package io.ashdavies.notion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.MosaicScope
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.delay

public suspend fun MosaicScope.NotionShell(args: Array<String>) {
    var state by mutableStateOf<NotionState>(NotionState.Initialising)
    var elapsed by mutableStateOf(0)

    setContent {
        Text("Time: $elapsed")

        when (val it = state) {
            is AuthState.Awaiting -> Text("Awaiting...")
            else -> Unit
        }

        NotionConsole(args) { state = it }
    }

    while (state !is AuthState.Authenticated && state !is SearchState.Complete) {
        delay(1_000)
        elapsed++
    }
}
