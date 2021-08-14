package androidx.lifecycle

internal fun <T> ViewModel.setOrDefault(key: String, default: () -> T): T =
    getTag(key) ?: setTagIfAbsent(key, default())
