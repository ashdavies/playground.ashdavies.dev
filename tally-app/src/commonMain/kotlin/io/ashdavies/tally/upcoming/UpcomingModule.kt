package io.ashdavies.tally.upcoming

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ashdavies.tally.events.paging.EventPager

internal object UpcomingModule {

    fun upcomingPresenterFactory(
        eventPager: EventPager,
        remoteAnalytics: RemoteAnalytics,
    ): Presenter.Factory = presenterFactoryOf<UpcomingScreen, UpcomingScreen.State> { _, _ ->
        UpcomingPresenter(
            eventPager = eventPager,
            remoteAnalytics = remoteAnalytics,
        )
    }

    fun upcomingUiFactory(
        windowSizeClass: WindowSizeClass,
    ): Ui.Factory = uiFactoryOf<UpcomingScreen, UpcomingScreen.State> { state, modifier ->
        UpcomingEventsScreen(
            state = state,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    }
}
