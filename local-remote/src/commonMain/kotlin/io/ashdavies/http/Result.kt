package io.ashdavies.http

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@PublishedApi
internal class LoadingException(val progress: Float) : Exception()

public val Result<*>.isLoading: Boolean
    get() = exceptionOrNull() is LoadingException

public inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R = fold({ it }) {
    when (it) {
        is T -> transform(it)
        else -> throw it
    }
}

public fun <T> Flow<Result<T>>.filterIsSuccess(): Flow<T> = filterIsSuccess { it }

public fun <T, R> Flow<Result<T>>.filterIsSuccess(transform: (T) -> R): Flow<R> = transform {
    it.onSuccess { value -> emit(transform(value)) }
}

public inline fun <T, R> Result<T>.fold(
    onFailure: (exception: Throwable) -> R,
    onLoading: (progress: Float) -> R,
    onSuccess: (value: T) -> R,
): R = fold(
    onSuccess = { onSuccess(it) },
    onFailure = {
        when (it) {
            is LoadingException -> onLoading(it.progress)
            else -> onFailure(it)
        }
    },
)

public fun <T> Result.Companion.loading(progress: Float = 0f): Result<T> = failure(LoadingException(progress))

@OptIn(ExperimentalContracts::class)
public inline fun <T> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    val exception = exceptionOrNull() ?: return this
    if (!isLoading) action(exception)
    return this
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> Result<T>.onLoading(action: (progress: Float) -> Unit): Result<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (isLoading) action((exceptionOrNull() as LoadingException).progress)
    return this
}

public inline fun <R, reified T : Throwable> runCatching(block: () -> R, transform: (T) -> Nothing): R {
    return runCatching(block) catch transform
}
