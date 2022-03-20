package io.ashdavies.playground.network

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayAt

public fun todayAsString(): String = Clock.System
    .todayAt(currentSystemDefault())
    .toString()
