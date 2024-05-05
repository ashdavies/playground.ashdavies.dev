package io.ashdavies.dominion

internal fun String.firstGroupOrNull(regex: Regex): String? = regex
    .find(this)
    ?.groupValues
    ?.get(1)