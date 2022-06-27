package io.ashdavies.http

public val Result<*>.isLoading: Boolean
    get() = exceptionOrNull() is LoadingException

public fun <T> Result.Companion.loading(progress: Float = 0f): Result<T> =
    failure(LoadingException(progress))

@PublishedApi
internal class LoadingException(val progress: Float) : Exception()

public inline fun <R, T> Result<T>.fold(
    onFailure: (exception: Throwable) -> R,
    onSuccess: (value: T) -> R,
    onLoading: (Float) -> R,
): R = fold(
    onFailure = { if (it is LoadingException) onLoading(it.progress) else onFailure(it) },
    onSuccess = { onSuccess(it) },
)
