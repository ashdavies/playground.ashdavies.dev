package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.playground.TokenQueries

@Composable
internal fun rememberTokenQueries(
    database: PlaygroundDatabase = LocalPlaygroundDatabase.current,
): TokenQueries = remember(database) {
    database.tokenQueries
}
