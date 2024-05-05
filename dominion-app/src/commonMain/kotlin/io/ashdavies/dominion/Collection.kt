package io.ashdavies.dominion

internal inline fun <T, K, V> Collection<T>.associateNotNull(
    transform: (T) -> Pair<K, V>?,
): Map<K, V> = buildMap {
    for (element in this@associateNotNull) {
        val pair = transform(element) ?: continue
        put(pair.first, pair.second)
    }
}
