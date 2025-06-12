package io.ashdavies.tally.upcoming

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.paging.Pager
import io.ashdavies.paging.rememberPagingState
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ashdavies.tally.coroutines.rememberRetainedCoroutineScope
import io.ashdavies.tally.events.Event

@ContributesTo(AppScope::class)
internal interface UpcomingModule {

    @IntoSet
    @Provides
    fun upcomingPresenterFactory(
        eventPager: Pager<*, Event>,
        remoteAnalytics: RemoteAnalytics,
    ): Presenter.Factory = presenterFactoryOf<UpcomingScreen, UpcomingScreen.State> { _, _ ->
        UpcomingPresenter(
            pagingState = rememberPagingState(
                retainedCoroutineScope = rememberRetainedCoroutineScope(),
                pager = eventPager,
            ),
            remoteAnalytics = remoteAnalytics,
        )
    }

    @IntoSet
    @Provides
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
