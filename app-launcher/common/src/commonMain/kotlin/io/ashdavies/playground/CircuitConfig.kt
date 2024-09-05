package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.common.PlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.dominion.addDominionBoxSetDetailsPresenter
import io.ashdavies.dominion.addDominionBoxSetDetailsUi
import io.ashdavies.dominion.addDominionBoxSetListPresenter
import io.ashdavies.dominion.addDominionBoxSetListUi
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.routes.addRoutePresenter
import io.ashdavies.routes.addRouteUi
import io.ashdavies.sql.LocalTransacter
import io.ktor.client.HttpClient

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    playgroundDatabase: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    httpClient: HttpClient = LocalHttpClient.current,
): Circuit = remember(platformContext) {
    Circuit.Builder()
        .addPresenter<LauncherScreen, LauncherScreen.State> { _, navigator, _ ->
            presenterOf { LauncherPresenter(navigator) }
        }
        .addDominionBoxSetListPresenter(playgroundDatabase, httpClient)
        .addDominionBoxSetDetailsPresenter(playgroundDatabase, httpClient)
        .addRoutePresenter(platformContext)
        .addUi<LauncherScreen, LauncherScreen.State> { state, modifier ->
            LauncherScreen(state, modifier)
        }
        .addDominionBoxSetListUi()
        .addDominionBoxSetDetailsUi()
        .addRouteUi(platformContext)
        .build()
}
