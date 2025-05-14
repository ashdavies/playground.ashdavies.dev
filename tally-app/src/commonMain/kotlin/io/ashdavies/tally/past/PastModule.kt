package io.ashdavies.tally.past

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.aggregator.PastConferencesCallable
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ashdavies.tally.events.AttendanceQueries
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface PastModule {

    @Provides
    fun attendanceQueries(database: PlaygroundDatabase): AttendanceQueries {
        return database.attendanceQueries
    }

    @IntoSet
    @Provides
    fun pastPresenterFactory(
        httpClient: HttpClient,
        attendanceQueries: AttendanceQueries,
    ): Presenter.Factory = presenterFactoryOf<PastScreen, PastScreen.State> { _, _ ->
        PastPresenter(
            pastConferencesCallable = PastConferencesCallable(httpClient),
            attendanceQueries = attendanceQueries,
            ioDispatcher = Dispatchers.IO,
        )
    }

    @IntoSet
    @Provides
    fun pastUiFactory(
        windowSizeClass: WindowSizeClass,
    ): Ui.Factory = uiFactoryOf<PastScreen, PastScreen.State> { state, modifier ->
        PastScreen(
            state = state,
            windowSizeClass = windowSizeClass,
            modifier = modifier,
        )
    }
}
