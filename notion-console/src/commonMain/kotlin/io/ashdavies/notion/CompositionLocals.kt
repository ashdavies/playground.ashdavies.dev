package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.ashdavies.playground.rememberDatabase
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

internal val LocalAuthCredentials = compositionLocalOf<AuthCredentials> {
    error("CompositionLocal LocalAuthCredentials not present")
}

internal val LocalNotionClient = compositionLocalOf<NotionClient> {
    error("CompositionLocal LocalNotionClient not present")
}

internal val LocalPlaygroundDatabase = compositionLocalOf<PlaygroundDatabase> {
    error("CompositionLocal LocalPlaygroundDatabase not present")
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
internal fun NotionCompositionLocals(
    authentication: Authentication,
    credentials: AuthCredentials,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNotionClient provides NotionClient.newInstance(
            configuration = ClientConfiguration(
                authentication = authentication,
            ),
        ),
        LocalAuthCredentials provides credentials,
        content = content,
    )
}
