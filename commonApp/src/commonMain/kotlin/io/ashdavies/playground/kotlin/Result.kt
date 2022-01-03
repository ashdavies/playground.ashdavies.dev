package io.ashdavies.playground.kotlin

val <T> Result<T>.isLoading: Boolean
    get() = exceptionOrNull() is LoadingException

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onLoading: () -> R,
    onFailure: (exception: Throwable) -> R,
): R = fold(
    onFailure = { if (it is LoadingException) onLoading() else onFailure(it) },
    onSuccess = { onSuccess(it) },
)

fun <T> Result.Companion.loading(): Result<T> =
    failure(LoadingException())

@PublishedApi
internal class LoadingException : Exception()