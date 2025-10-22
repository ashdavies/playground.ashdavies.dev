package dev.ashdavies.playground.adaptive

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.playground.circuit.CircuitScreenKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding

internal class ListDetailScaffoldPresenter @AssistedInject constructor(
    @Assisted private val screen: ListDetailScaffoldScreen,
) : Presenter<ListDetailScaffoldScreen.State> {

    @Composable
    override fun present() = ListDetailScaffoldScreen.State(
        initialScreen = screen.initialScreen,
    )

    @AssistedFactory
    @CircuitScreenKey(ListDetailScaffoldScreen::class)
    @ContributesIntoMap(AppScope::class, binding<(Screen) -> Presenter<*>>())
    interface Factory : (ListDetailScaffoldScreen) -> ListDetailScaffoldPresenter
}
