package io.ashdavies.playground.arch

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal interface ViewEvent

internal interface ViewEventStore<T : ViewEvent> {

    val viewEvents: Flow<T>

    infix fun post(viewEvent: T)
}

internal fun <T : ViewEvent> ViewEventStore(): ViewEventStore<T> = object : ViewEventStore<T> {

    private val _viewEvents = Channel<T>(CONFLATED)
    override val viewEvents: Flow<T> =
        _viewEvents.receiveAsFlow()

    override fun post(viewEvent: T) {
        _viewEvents.trySend(viewEvent)
    }
}
