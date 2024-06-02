package io.ashdavies.party

import io.ashdavies.content.PlatformContext
import io.ashdavies.sql.DatabaseFactory

internal fun PlaygroundDatabase(context: PlatformContext): PlaygroundDatabase = DatabaseFactory(
    schema = PlaygroundDatabase.Schema,
    context = context,
    factory = { PlaygroundDatabase(it) },
)
