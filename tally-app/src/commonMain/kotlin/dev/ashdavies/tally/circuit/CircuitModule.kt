package dev.ashdavies.tally.circuit

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass

@ContributesTo(AppScope::class)
internal interface CircuitModule {

    @Multibinds
    val screenNavigatorPresenterFactories: Map<KClass<out Screen>, (Screen, Navigator) -> Presenter<*>>

    @Multibinds
    val screenPresenterFactories: Map<KClass<out Screen>, (Screen) -> Presenter<*>>

    @Multibinds
    val navigatorPresenterFactories: Map<KClass<out Screen>, (Navigator) -> Presenter<*>>

    @Multibinds
    val presenterFactories: Map<KClass<out Screen>, Provider<Presenter<*>>>

    @Multibinds
    val uiFactories: Map<KClass<out Screen>, Provider<Ui<*>>>

    @Provides
    fun circuit(
        screenNavigatorPresenters: @JvmSuppressWildcards Map<KClass<out Screen>, (Screen, Navigator) -> Presenter<*>>,
        screenPresenters: @JvmSuppressWildcards Map<KClass<out Screen>, (Screen) -> Presenter<*>>,
        navigatorPresenters: @JvmSuppressWildcards Map<KClass<out Screen>, (Navigator) -> Presenter<*>>,
        presenterFactories: @JvmSuppressWildcards Map<KClass<out Screen>, Provider<Presenter<*>>>,
        uiFactories: @JvmSuppressWildcards Map<KClass<out Screen>, Provider<Ui<*>>>,
    ): Circuit = Circuit.Builder()
        .addPresenterFactory { screen, navigator, _ ->
            screenNavigatorPresenters[screen::class]?.invoke(screen, navigator)
        }
        .addPresenterFactory { screen, _, _ ->
            screenPresenters[screen::class]?.invoke(screen)
        }
        .addPresenterFactory { screen, navigator, _ ->
            navigatorPresenters[screen::class]?.invoke(navigator)
        }
        .addPresenterFactory { screen, _, _ ->
            presenterFactories[screen::class]?.invoke()
        }
        .addUiFactory { screen, context ->
            uiFactories[screen::class]?.invoke()
        }
        .build()
}
