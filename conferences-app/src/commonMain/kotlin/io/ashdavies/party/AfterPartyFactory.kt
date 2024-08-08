package io.ashdavies.party

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn

public fun afterPartyPresenterFactory(context: PlatformContext): Presenter.Factory {
    return presenterFactoryOf<AfterPartyScreen> { _, navigator ->
        AfterPartyPresenter(context, navigator)
    }
}

public fun afterPartyUiFactory(context: PlatformContext): Ui.Factory {
    return uiFactoryOf<AfterPartyScreen, AfterPartyScreen.State> { _, state, modifier ->
        AfterPartyScreen(state, modifier)
        context.reportFullyDrawn()
    }
}
