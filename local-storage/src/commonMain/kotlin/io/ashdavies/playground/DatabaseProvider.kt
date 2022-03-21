package io.ashdavies.playground

public val LocalPlaygroundDatabase: ComposableCompositionLocal<PlaygroundDatabase> = composableCompositionLocalOf {
    DatabaseFactory(PlaygroundDatabase.Schema) { PlaygroundDatabase(it) }
}
