package io.ashdavies.check

/**
 * @see kotlinx.coroutines.InternalCoroutinesApi
 */
@Target(AnnotationTarget.CLASS)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(value = AnnotationRetention.BINARY)
public annotation class InternalAppCheckApi
