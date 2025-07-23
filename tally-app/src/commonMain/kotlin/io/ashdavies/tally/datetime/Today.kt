package io.ashdavies.tally.datetime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal val Today = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date
