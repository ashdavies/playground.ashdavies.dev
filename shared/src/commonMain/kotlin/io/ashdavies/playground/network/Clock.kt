package io.ashdavies.playground.network

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayAt

fun todayAsString() = Clock.System
    .todayAt(currentSystemDefault())
    .toString()
