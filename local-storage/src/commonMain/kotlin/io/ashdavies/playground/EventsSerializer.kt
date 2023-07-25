package io.ashdavies.playground

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

/**
 * TODO: Move to events domain
 */
@ExperimentalSerializationApi
@Serializer(forClass = Event::class)
public object EventsSerializer
