package io.ashdavies.notion.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.notion.PlaygroundDatabase
import io.ashdavies.playground.TokenQueries

@Composable
internal fun rememberTokenQueries(
    database: PlaygroundDatabase = LocalPlaygroundDatabase.current,
): TokenQueries = remember(database) { database.tokenQueries }
