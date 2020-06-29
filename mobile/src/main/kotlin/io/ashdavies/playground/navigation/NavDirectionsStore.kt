package io.ashdavies.playground.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.Flow

internal interface NavDirectionsStore {

    val navDirections: Flow<NavDirections>
}
