package io.ashdavies.notion

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import kotlinx.coroutines.delay

public fun main(args: Array<String>): Unit = runMosaic {
    setContent {
        var elapsed by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1_000)
                elapsed++
            }
        }

        NotionConsole(args)

        Text("Time: $elapsed")
    }
}
