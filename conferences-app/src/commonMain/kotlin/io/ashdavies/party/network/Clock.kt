package io.ashdavies.party.network

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayIn

public fun todayAsString(): String = Clock.System
    .todayIn(currentSystemDefault())
    .toString()
