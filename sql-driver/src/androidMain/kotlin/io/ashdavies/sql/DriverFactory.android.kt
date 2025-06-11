package io.ashdavies.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.ashdavies.content.PlatformContext
import app.cash.sqldelight.async.coroutines.synchronous

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver = AndroidSqliteDriver(
        schema = schema.synchronous(),
        context = context,
        name = name,
    )
}
