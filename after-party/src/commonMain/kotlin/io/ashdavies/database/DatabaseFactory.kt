package io.ashdavies.database

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext
import io.ashdavies.sql.DatabaseFactory

@Composable
internal fun <T : Transacter> rememberDatabase(
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
