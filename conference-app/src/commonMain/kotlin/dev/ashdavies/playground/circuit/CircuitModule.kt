package dev.ashdavies.playground.circuit

import androidx.savedstate.serialization.SavedStateConfiguration
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.CircuitSaveable
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.serialization.SerializableCircuitSaver
import dev.ashdavies.playground.adaptive.ListDetailScaffoldScreen
import dev.ashdavies.playground.event.EventScreen
import dev.ashdavies.playground.gallery.GalleryScreen
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.ashdavies.playground.routes.RoutesScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@ContributesTo(AppScope::class)
internal interface CircuitModule {

    @Multibinds
    fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds
    fun uiFactories(): Set<Ui.Factory>

    @Provides
    fun circuit(
        savedStateConfiguration: SavedStateConfiguration,
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit = Circuit.Builder()
        .setCircuitSaver(SerializableCircuitSaver(savedStateConfiguration))
        .addPresenterFactories(presenterFactories)
        .addUiFactories(uiFactories)
        .build()

    @Provides
    @OptIn(ExperimentalSerializationApi::class)
    fun savedStateConfiguration(): SavedStateConfiguration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(CircuitSaveable::class) {
                subclass(BottomBarScaffoldScreen::class)
                subclass(GalleryScreen::class)
                subclass(ListDetailScaffoldScreen::class)
                subclass(RoutesScreen::class)

                subclassesOfSealed<EventScreen>()
            }
        }
    }
}
