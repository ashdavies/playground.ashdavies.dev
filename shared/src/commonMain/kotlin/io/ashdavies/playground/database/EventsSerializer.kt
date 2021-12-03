package io.ashdavies.playground.database

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Event::class)
object EventsSerializer
