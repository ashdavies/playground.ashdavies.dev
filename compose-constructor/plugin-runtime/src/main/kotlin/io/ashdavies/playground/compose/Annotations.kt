package io.ashdavies.playground.compose

import kotlin.reflect.KClass

@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.BINARY)
public annotation class Remember

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
public annotation class Remembering(
    val forClass: KClass<*>
)
