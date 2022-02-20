package io.ashdavies.playground

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Event::class)
object EventsSerializer
