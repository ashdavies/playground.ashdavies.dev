package io.ashdavies.sql

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

@Composable
public fun <T : Transacter> rememberTransacter(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    context: PlatformContext,
    factory: (SqlDriver) -> T,
): T = remember(schema, context) {
    DatabaseFactory(
        schema = schema,
        context = context,
        factory = factory,
    )
}
