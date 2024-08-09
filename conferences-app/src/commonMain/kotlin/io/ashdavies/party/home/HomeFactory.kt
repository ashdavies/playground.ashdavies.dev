package io.ashdavies.party.home

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn

public fun homePresenterFactory(context: PlatformContext): Presenter.Factory {
    return presenterFactoryOf<HomeScreen> { _, navigator ->
        HomePresenter(context, navigator)
    }
}

public fun homeUiFactory(context: PlatformContext): Ui.Factory {
    return uiFactoryOf<HomeScreen, HomeScreen.State> { _, state, modifier ->
        HomeScreen(state, modifier)
        context.reportFullyDrawn()
    }
}
