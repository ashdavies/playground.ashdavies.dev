package dev.ashdavies.cloud.events

import dev.zacsweers.metro.MapKey

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
internal annotation class EventAggregateKey(val source: EventAggregateSource)
