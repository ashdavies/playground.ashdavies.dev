package io.ashdavies.tally.circuit

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass

@ContributesTo(AppScope::class)
internal interface CircuitModule {

    @Multibinds
    val presenterFactories: Map<KClass<out Screen>, Presenter<*>>

    @Multibinds
    val uiFactories: Map<KClass<out Screen>, Ui<*>>

    companion object {

        @Provides
        fun circuit(
            presenterFactories: @JvmSuppressWildcards Map<KClass<out Screen>, Presenter<*>>,
            uiFactories: @JvmSuppressWildcards Map<KClass<out Screen>, Ui<*>>,
        ): Circuit = Circuit.Builder()
            .addPresenterFactory { screen, _, _ -> presenterFactories[screen::class] } // TODO Provider?
            .addUiFactory { screen, context -> uiFactories[screen::class] }
            .build()
    }
}
