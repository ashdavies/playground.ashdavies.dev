package io.ashdavies.cloud

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QuerySnapshot
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

public suspend fun Query.await(): QuerySnapshot = get().await()

public suspend fun <T> ApiFuture<T>.await(
    context: CoroutineContext = Dispatchers.IO,
): T = when {
    isDone -> try {
        withContext(context) { get() as T }
    } catch (exception: ExecutionException) {
        throw exception.cause ?: exception
    }

    else -> suspendCancellableCoroutine { continuation ->
        val callback = ContinuationCallback(continuation)
        addCallback(callback, MoreExecutors.directExecutor())

        continuation.invokeOnCancellation {
            cancel(/* mayInterruptIfRunning = */ false)
            callback.continuation = null
        }
    }
}

private fun <T> ApiFuture<T>.addCallback(
    callback: ApiFutureCallback<T>,
    executors: Executor,
) = ApiFutures.addCallback(
    /* future = */ this,
    /* callback = */ callback,
    /* executor = */ executors,
)

private class ContinuationCallback<T>(
    @Volatile @JvmField var continuation: Continuation<T>?,
) : ApiFutureCallback<T> {
    override fun onSuccess(result: T?) {
        @Suppress("UNCHECKED_CAST")
        continuation?.resume(result as T)
    }

    override fun onFailure(t: Throwable) {
        continuation?.resumeWithException(t)
    }
}
