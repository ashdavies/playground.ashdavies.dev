package io.ashdavies.playground.database

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@ExperimentalSerializationApi
@Serializer(forClass = Conference::class)
object ConferenceSerializer