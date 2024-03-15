package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.database.rememberDatabase
import io.ashdavies.party.PlaygroundDatabase

internal val LocalPlaygroundDatabase = compositionLocalOf<PlaygroundDatabase> {
    error("CompositionLocal LocalPlaygroundDatabase not present")
}

@Composable
public fun ProvidePlaygroundDatabase(
    context: PlatformContext,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPlaygroundDatabase provides rememberDatabase(
            factory = PlaygroundDatabase.Companion::invoke,
            context = context,
            schema = PlaygroundDatabase.Schema,
        ),
        content = content,
    )
}
