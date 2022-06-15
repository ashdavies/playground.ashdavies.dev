package io.ashdavies.playground

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

/**
 * TODO: Move to events domain
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Event::class)
public object EventsSerializer
