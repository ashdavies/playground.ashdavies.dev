package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.appcheck.AppCheckToken
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import io.ktor.http.content.OutgoingContent.NoContent

internal interface AppCheckService : PlaygroundService {
    val createToken: PlaygroundService.Operator<NoContent, AppCheckToken>
}

@Composable
internal fun rememberAppCheckService(client: HttpClient = LocalHttpClient.current): AppCheckService = remember(client) {
    object : AppCheckService, PlaygroundService by PlaygroundService(client) {
        override val createToken by getting<NoContent, AppCheckToken>()
    }
}
