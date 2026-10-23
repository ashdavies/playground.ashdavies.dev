package dev.ashdavies.playground

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.serialization.CircuitSerializerRegistration
import com.slack.circuit.serialization.SerializableCircuitSaver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
internal interface CircuitModule {

    @Multibinds
    fun circuitSerializerRegistrations(): Set<CircuitSerializerRegistration>

    @Multibinds
    fun presenterFactories(): Set<Presenter.Factory>

    @Multibinds
    fun uiFactories(): Set<Ui.Factory>

    @Provides
    fun circuit(
        circuitSerializerRegistrations: Set<CircuitSerializerRegistration>,
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit = Circuit.Builder()
        .setCircuitSaver(SerializableCircuitSaver(circuitSerializerRegistrations))
        .addPresenterFactories(presenterFactories)
        .addUiFactories(uiFactories)
        .build()
}
