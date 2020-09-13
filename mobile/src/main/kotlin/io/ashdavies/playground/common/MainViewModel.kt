package io.ashdavies.playground.common

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import io.ashdavies.playground.navigation.NavDirectionsStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class MainViewModel : NavDirectionsStore, ViewModel() {

    private val _navDirections: Channel<NavDirections> = Channel(CONFLATED)
    override val navDirections: Flow<NavDirections> get() = _navDirections.receiveAsFlow()
}
