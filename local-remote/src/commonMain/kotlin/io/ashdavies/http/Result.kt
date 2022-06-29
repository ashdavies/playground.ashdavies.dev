package io.ashdavies.http

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public val Result<*>.isLoading: Boolean
    get() = exceptionOrNull() is LoadingException

public fun <T> Result.Companion.loading(progress: Float = 0f): Result<T> =
    failure(LoadingException(progress))

@PublishedApi
internal class LoadingException(val progress: Float) : Exception()

public inline fun <R, T> Result<T>.fold(
    onFailure: (exception: Throwable) -> R,
    onLoading: (progress: Float) -> R,
    onSuccess: (value: T) -> R,
): R = fold(
    onFailure = { if (it is LoadingException) onLoading(it.progress) else onFailure(it) },
    onSuccess = { onSuccess(it) },
)

@OptIn(ExperimentalContracts::class)
public inline fun <T> Result<T>.onLoading(action: (progress: Float) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isLoading) action((exceptionOrNull() as LoadingException).progress)
    return this
}

public inline fun <R, reified T : Throwable> runCatching(block: () -> R, transform: (T) -> Nothing): R {
    return runCatching(block) catch transform
}

public inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R {
    return fold({ it }) { if (it is T) transform(it) else throw it }
}
