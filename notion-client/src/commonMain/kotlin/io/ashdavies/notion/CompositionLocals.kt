package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.compose.noLocalProvidedFor
import io.ashdavies.playground.rememberDatabase
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.NotionClient

public val LocalNotionClient: ProvidableCompositionLocal<NotionClient> =
    compositionLocalOf { noLocalProvidedFor("LocalNotionClient") }

public val LocalPlaygroundDatabase: ProvidableCompositionLocal<PlaygroundDatabase> =
    compositionLocalOf { noLocalProvidedFor("LocalPlaygroundDatabase") }

@Composable
public fun NotionCompositionLocals(auth: Authentication, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNotionClient provides NotionClient(auth),
        LocalPlaygroundDatabase provides rememberDatabase(
            schema = PlaygroundDatabase.Schema,
            factory = PlaygroundDatabase::invoke,
        ),
        content = content,
    )
}
