package io.ashdavies.playground.extensions

import androidx.fragment.app.Fragment
import io.ashdavies.playground.navController
import io.ashdavies.playground.navigation.NavDirector
import kotlinx.coroutines.flow.collect

internal suspend fun Fragment.navigate(director: NavDirector) = director
    .directions
    .collect { navController.navigate(it) }