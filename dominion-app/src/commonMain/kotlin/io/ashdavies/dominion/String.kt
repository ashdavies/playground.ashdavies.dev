package io.ashdavies.dominion

internal fun String.firstGroup(regex: Regex): String {
    return firstGroupOrNull(regex) ?: error("No groups found for regex '$regex'")
}

internal fun String.firstGroupOrNull(regex: Regex): String? = regex
    .find(this)
    ?.groupValues
    ?.get(1)
