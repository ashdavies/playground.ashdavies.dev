package dev.ashdavies.playground.adaptive

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

internal class ListDetailScaffoldPresenter @AssistedInject constructor(
    @Assisted private val screen: ListDetailScaffoldScreen,
) : Presenter<ListDetailScaffoldScreen.State> {

    @Composable
    override fun present() = ListDetailScaffoldScreen.State(
        initialScreen = screen.initialScreen,
    )

    @AssistedFactory
    @CircuitInject(ListDetailScaffoldScreen::class, AppScope::class)
    interface Factory : (ListDetailScaffoldScreen) -> ListDetailScaffoldPresenter {
        override operator fun invoke(screen: ListDetailScaffoldScreen): ListDetailScaffoldPresenter
    }
}
