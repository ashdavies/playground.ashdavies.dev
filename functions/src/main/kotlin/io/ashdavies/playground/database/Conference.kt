package io.ashdavies.playground.database

import io.ashdavies.playground.firebase.DocumentData

internal typealias Conference = DocumentData

internal val Conference.id: String
    get() = asDynamic().id as String
