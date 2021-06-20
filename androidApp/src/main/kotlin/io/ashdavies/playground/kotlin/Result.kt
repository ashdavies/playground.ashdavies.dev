package io.ashdavies.playground.kotlin

import com.google.accompanist.imageloading.ImageLoadState

val <T> Result<T>.isLoading: Boolean get() = getOrNull() is ImageLoadState.Loading

internal inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onLoading: () -> R,
    onFailure: (exception: Throwable) -> R,
): R = fold(
    onFailure = { if (it is LoadingException) onLoading() else onFailure(it) },
    onSuccess = onSuccess,
)

internal fun <T> Result.Companion.loading(): Result<T> = failure<T>(LoadingException())

private class LoadingException : Exception()