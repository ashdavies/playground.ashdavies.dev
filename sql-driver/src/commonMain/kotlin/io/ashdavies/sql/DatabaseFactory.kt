package io.ashdavies.sql

import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public object DatabaseFactory {
    public operator fun <S : SqlSchema<QueryResult.AsyncValue<Unit>>, T : SuspendingTransacter> invoke(
        schema: S,
        context: PlatformContext,
        factory: (SqlDriver) -> T,
    ): T = DriverFactory(
        schema = schema,
        context = context,
        name = "database.db",
    ).let(factory)
}
