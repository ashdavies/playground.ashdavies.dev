package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn
import io.ashdavies.routes.addRoutePresenter
import io.ashdavies.routes.addRouteUi

@Composable
public fun rememberCircuit(platformContext: PlatformContext): Circuit = remember(platformContext) {
    Circuit.Builder()
        .addLauncherPresenter()
        .addRoutePresenter(platformContext)
        .addLauncherUi(platformContext::reportFullyDrawn)
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
