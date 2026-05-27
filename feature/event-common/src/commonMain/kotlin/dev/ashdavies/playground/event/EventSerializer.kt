package dev.ashdavies.playground.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@ExperimentalSerializationApi
@Serializer(forClass = Event::class)
public object EventSerializer
