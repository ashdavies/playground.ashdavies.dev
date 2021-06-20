package io.ashdavies.playground.database

import kotlinx.serialization.Serializer

@Serializer(forClass = Event::class)
object EventsSerializer