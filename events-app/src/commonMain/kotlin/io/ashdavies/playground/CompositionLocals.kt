package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.compose.noLocalProvidedFor
import io.ashdavies.events.PlaygroundDatabase

internal val LocalPlaygroundDatabase = compositionLocalOf<PlaygroundDatabase> {
    noLocalProvidedFor("LocalPlaygroundDatabase")
}

@Composable
internal fun EventsCompositionLocals(content: @Composable () -> Unit) {
    val database = rememberDatabase(
        schema = PlaygroundDatabase.Schema,
        factory = PlaygroundDatabase::invoke,
    )

    CompositionLocalProvider(
        LocalPlaygroundDatabase provides database,
        content = content,
    )
}
