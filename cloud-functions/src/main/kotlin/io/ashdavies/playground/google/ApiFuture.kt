package io.ashdavies.playground.google

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures.addCallback
import com.google.common.util.concurrent.MoreExecutors.directExecutor
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

public suspend fun <T> ApiFuture<T>.await(): T {
    return suspendCancellableCoroutine { continuation: CancellableContinuation<T> ->
        addCallback(this, object : ApiFutureCallback<T> {
            override fun onFailure(throwable: Throwable) = continuation.resumeWithException(throwable)
            override fun onSuccess(result: T) = continuation.resume(result)
        }, directExecutor())

        continuation.invokeOnCancellation {
            cancel(false)
        }
    }
}
