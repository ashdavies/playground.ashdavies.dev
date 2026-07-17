package dev.ashdavies.playground.gallery

import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.parcelable.Parcelable
import dev.ashdavies.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
public object GalleryScreen : Parcelable, Screen
