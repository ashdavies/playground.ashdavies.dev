package io.ashdavies.playground.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.Flow

internal interface NavDirector {

    val directions: Flow<NavDirections>

    fun navigate(directions: NavDirections)
}
