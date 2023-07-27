package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

@Composable
public fun <T : Transacter> rememberDatabase(
    schema: SqlSchema<QueryResult.Value<Unit>>,
    factory: (SqlDriver) -> T,
): T {
    val config = getDriverConfig()
    return remember(schema, config) {
        DatabaseFactory(
            factory = factory,
            schema = schema,
            config = config,
        )
    }
}
