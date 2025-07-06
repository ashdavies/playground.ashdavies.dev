package io.ashdavies.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public expect object DriverFactory {
    public suspend operator fun invoke(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver
}
