package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.common.PlaygroundDatabase
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.dominion.addDominionPresenter
import io.ashdavies.dominion.addDominionUi
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
        .addLauncherPresenter()
        .addDominionPresenter(playgroundDatabase, httpClient)
        .addRoutePresenter(platformContext)
        .addLauncherUi(platformContext::reportFullyDrawn)
        .addDominionUi()
        .addRouteUi()
        .build()
}

private fun Circuit.Builder.addLauncherPresenter(): Circuit.Builder {
    return addPresenter<LauncherScreen, LauncherScreen.State> { _, navigator, _ ->
        presenterOf { LauncherPresenter(navigator) }
    }
}

private fun Circuit.Builder.addLauncherUi(reportFullyDrawn: () -> Unit): Circuit.Builder {
    return addUi<LauncherScreen, LauncherScreen.State> { state, modifier ->
        LauncherScreen(state, modifier, reportFullyDrawn)
    }
}
