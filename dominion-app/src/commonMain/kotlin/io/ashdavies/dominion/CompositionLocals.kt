package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.sqldelight.Transacter
import io.ashdavies.sql.LocalTransacter

@Composable
internal fun <T : Transacter> rememberLocalQueries(
    database: PlaygroundDatabase = LocalTransacter.current as PlaygroundDatabase,
    transform: (PlaygroundDatabase) -> T,
): T = remember { transform(database) }
