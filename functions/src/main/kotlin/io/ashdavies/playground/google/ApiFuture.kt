package io.ashdavies.playground.google

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures.addCallback
import com.google.common.util.concurrent.MoreExecutors.directExecutor
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ExecutionException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal suspend fun <T> ApiFuture<T>.await(): T {
    try {
        if (isDone) return get() as T
    } catch (exception: ExecutionException) {
        throw exception.cause ?: exception
    }

    return suspendCancellableCoroutine { continuation: CancellableContinuation<T> ->
        val callback = ContinuationCallback(continuation)
        addCallback(this, callback, directExecutor())
        continuation.invokeOnCancellation {
            callback.continuation = null
            cancel(false)
        }
    }
}

private class ContinuationCallback<T>(
    @Volatile var continuation: Continuation<T>?,
) : ApiFutureCallback<T> {
    override fun onSuccess(result: T?) {
        @Suppress("UNCHECKED_CAST")
        continuation?.resume(result as T)
    }

    override fun onFailure(throwable: Throwable) {
        continuation?.resumeWithException(throwable)
    }
}