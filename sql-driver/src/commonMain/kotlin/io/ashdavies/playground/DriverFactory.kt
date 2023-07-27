package io.ashdavies.playground

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

public expect class DriverConfig

public expect object DriverFactory {
    public operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        config: DriverConfig,
    ): SqlDriver
}
