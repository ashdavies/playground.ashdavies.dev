package io.ashdavies.notion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import kotlinx.coroutines.delay

public fun main(args: Array<String>): Unit = runMosaic {
    var state by mutableStateOf<NotionState>(NotionState.Initialising)
    var elapsed by mutableStateOf(0)

    setContent {
        Text("Time: $elapsed")

        (state as? AuthState.Awaiting)?.also {
            Text("Navigate to ${it.userPromptUri} to continue")
        }

        NotionConsole(args) { state = it }
    }

    while (state !is AuthState.Authenticated && state !is SearchState.Complete) {
        delay(1_000)
        elapsed++
    }
}
