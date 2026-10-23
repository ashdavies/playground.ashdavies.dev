package dev.ashdavies.cloud.google

internal typealias FirestoreDocument = Map<String, Any?>

@Suppress("UNCHECKED_CAST")
internal val FirestoreDocument.attrs: Map<String, String>
    get() = getValue("_attrs") as Map<String, String>
