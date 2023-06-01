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
internal fun ProvidePlaygroundDatabase(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalPlaygroundDatabase provides rememberDatabase(
            factory = PlaygroundDatabase::invoke,
            schema = PlaygroundDatabase.Schema,
        ),
        content = content,
    )
}

@Composable
internal fun ProvideNotionClient(authentication: Authentication, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNotionClient provides NotionClient.newInstance(
            configuration = ClientConfiguration(
                authentication = authentication,
            ),
        ),
        content = content,
    )
}
