package io.ashdavies.tally.events

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@ExperimentalSerializationApi
@Serializer(forClass = Event::class)
public object EventsSerializer
