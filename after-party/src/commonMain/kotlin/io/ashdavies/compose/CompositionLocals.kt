package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.database.rememberDatabase
import io.ashdavies.party.PlaygroundDatabase

@RequiresOptIn(
    message = "The database and driver should only be created a single time.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
internal annotation class MultipleReferenceWarning

internal val LocalPlaygroundDatabase = compositionLocalOf<PlaygroundDatabase> {
    error("CompositionLocal LocalPlaygroundDatabase not present")
}

@Composable
@OptIn(MultipleReferenceWarning::class)
public fun ProvidePlaygroundDatabase(
    context: PlatformContext,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPlaygroundDatabase provides rememberPlaygroundDatabase(context),
        content = content,
    )
}

@Composable
@MultipleReferenceWarning
private fun rememberPlaygroundDatabase(
    context: PlatformContext,
): PlaygroundDatabase = rememberDatabase(
    factory = PlaygroundDatabase.Companion::invoke,
    context = context,
    schema = PlaygroundDatabase.Schema,
)
