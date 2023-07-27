package io.ashdavies.playground

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.db.QueryResult

public expect class DriverConfig

public expect object DriverFactory {
    public operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        config: DriverConfig,
    ): SqlDriver
}
