package io.ashdavies.playground

import com.slack.circuit.Screen
import io.ashdavies.dominion.DominionScreen
import kotlinx.collections.immutable.persistentListOf

public fun buildInitialBackStack(initialScreen: Screen, nextScreen: String? = null): List<Screen> {
    return when (nextScreen) {
        "events" -> persistentListOf(initialScreen, EventsScreen)
        "dominion" -> persistentListOf(initialScreen, DominionScreen)
        else -> persistentListOf(initialScreen)
    }
}
