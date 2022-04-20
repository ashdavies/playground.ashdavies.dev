package io.ashdavies.playground

import com.arkivanov.essenty.lifecycle.Lifecycle

internal class DefaultLifecycle(override val state: Lifecycle.State = Lifecycle.State.RESUMED) : Lifecycle {
    override fun subscribe(callbacks: Lifecycle.Callbacks) = Unit
    override fun unsubscribe(callbacks: Lifecycle.Callbacks) = Unit
}
