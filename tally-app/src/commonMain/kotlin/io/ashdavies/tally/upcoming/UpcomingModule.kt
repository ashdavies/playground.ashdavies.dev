package io.ashdavies.tally.upcoming

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.paging.Pager
import io.ashdavies.paging.PagingState
import io.ashdavies.paging.rememberPagingState
import io.ashdavies.sql.Suspended
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ashdavies.tally.coroutines.rememberRetainedCoroutineScope
import io.ashdavies.tally.events.Event
import kotlinx.coroutines.flow.asFlow

@ContributesTo(AppScope::class)
internal interface UpcomingModule {

    @IntoSet
    @Provides
    fun upcomingPresenterFactory(
        eventPager: Suspended<Pager<*, Event>>,
        remoteAnalytics: RemoteAnalytics,
    ): Presenter.Factory = presenterFactoryOf<UpcomingScreen, UpcomingScreen.State> { _, _ ->
        val eventPager by eventPager.asFlow().collectAsState(null)

        UpcomingPresenter(
            pagingState = when (val eventPager = eventPager) {
                is Pager<*, Event> -> rememberPagingState(
                    retainedCoroutineScope = rememberRetainedCoroutineScope(),
                    pager = eventPager,
                )

                else -> PagingState.empty()
            },
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
