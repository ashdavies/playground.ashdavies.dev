package io.ashdavies.playground

val LocalPlaygroundDatabase = composableCompositionLocalOf {
    DatabaseFactory(PlaygroundDatabase.Schema) { PlaygroundDatabase(it) }
}
