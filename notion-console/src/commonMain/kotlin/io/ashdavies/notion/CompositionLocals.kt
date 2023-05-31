package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.compose.noLocalProvidedFor
import io.ashdavies.playground.rememberDatabase
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

internal val LocalNotionClient = compositionLocalOf<NotionClient> {
    noLocalProvidedFor("LocalNotionClient")
}

internal val LocalPlaygroundDatabase = compositionLocalOf<PlaygroundDatabase> {
    noLocalProvidedFor("LocalPlaygroundDatabase")
}

@Composable
internal fun NotionCompositionLocals(auth: Authentication, content: @Composable () -> Unit) {
    val configuration = ClientConfiguration(auth)
    val client = NotionClient.newInstance(configuration)

    val database = rememberDatabase(
        schema = PlaygroundDatabase.Schema,
        factory = PlaygroundDatabase::invoke,
    )

    CompositionLocalProvider(
        LocalNotionClient provides client,
        LocalPlaygroundDatabase provides database,
        content = content,
    )
}
