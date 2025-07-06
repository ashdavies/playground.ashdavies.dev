package io.ashdavies.tally.past

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import io.ashdavies.asg.PastConferencesCallable
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.circuit.presenterFactoryOf
import io.ashdavies.tally.circuit.uiFactoryOf
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface PastModule {

    @IntoSet
    @Provides
    fun pastPresenterFactory(
        httpClient: HttpClient,
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    ): Presenter.Factory = presenterFactoryOf<PastScreen, PastScreen.State> { _, _ ->
        PastPresenter(
            pastConferencesCallable = PastConferencesCallable(httpClient),
            attendanceQueries = databaseFactory.map { it.attendanceQueries },
            coroutineContext = Dispatchers.Default,
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
