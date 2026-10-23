package dev.ashdavies.playground.gallery

import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.serialization.CircuitSerializable
import dev.zacsweers.metro.AppScope
import kotlinx.serialization.Serializable

@Serializable
@CircuitSerializable(AppScope::class)
public object GalleryScreen : Screen
