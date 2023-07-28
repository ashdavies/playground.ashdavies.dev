package io.ashdavies.playground

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public expect object DriverFactory {
    public operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver
}
