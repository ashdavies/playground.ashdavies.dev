package io.ashdavies.playground

import com.arkivanov.essenty.lifecycle.Lifecycle

public class DefaultLifecycle(override val state: Lifecycle.State = Lifecycle.State.RESUMED) : Lifecycle {
    override fun unsubscribe(callbacks: Lifecycle.Callbacks) {}
    override fun subscribe(callbacks: Lifecycle.Callbacks) {}
}
