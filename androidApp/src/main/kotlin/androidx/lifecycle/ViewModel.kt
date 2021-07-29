package androidx.lifecycle

fun <T> ViewModel.getOrDefault(key: String, default: () -> T): T =
    getTag(key) ?: setTagIfAbsent(key, default())
