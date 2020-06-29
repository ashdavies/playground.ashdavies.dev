package io.ashdavies.playground

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal fun LifecycleOwner.onDestroy(block: (LifecycleOwner) -> Unit) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) = block(owner)
    })
}
