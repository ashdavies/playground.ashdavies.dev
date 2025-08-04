package io.ashdavies.tally.home

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.tally.circuit.CircuitScreenKey

@CircuitScreenKey(ListDetailScaffoldScreen::class)
@ContributesIntoMap(AppScope::class, binding<Presenter<*>>())
internal class ListDetailScaffoldPresenter @Inject constructor() : Presenter<ListDetailScaffoldScreen.State> {

    @Composable
    override fun present() = ListDetailScaffoldScreen.State
}
