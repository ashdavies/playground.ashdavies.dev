package io.ashdavies.playground.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class ChannelNavDirector : NavDirector {

  private val _directions: Channel<NavDirections> = Channel(CONFLATED)

  override val directions: Flow<NavDirections> = flow {
    for (it: NavDirections in _directions) {
      emit(it)
    }
  }

  override fun navigate(directions: NavDirections) {
    _directions.offer(directions)
  }
}