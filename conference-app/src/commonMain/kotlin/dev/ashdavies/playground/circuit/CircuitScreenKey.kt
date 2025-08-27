package dev.ashdavies.playground.circuit

import com.slack.circuit.runtime.screen.Screen
import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class CircuitScreenKey(val value: KClass<out Screen>)
