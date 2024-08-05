package io.ashdavies.cloud

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures.addCallback
import com.google.common.util.concurrent.MoreExecutors.directExecutor
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

public suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { continuation ->
    val callback = object : ApiFutureCallback<T> {
        override fun onFailure(throwable: Throwable) = continuation.resumeWithException(throwable)
        override fun onSuccess(result: T) = continuation.resume(result)
    }

    addCallback(this, callback, directExecutor())

    continuation.invokeOnCancellation {
        cancel(false)
    }
}
