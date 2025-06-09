package io.ashdavies.sql

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public object DatabaseFactory {
    public suspend operator fun <S : SqlSchema<QueryResult.AsyncValue<Unit>>, T : Transacter> invoke(
        schema: S,
        context: PlatformContext,
        factory: (SqlDriver) -> T,
    ): T = DriverFactory(schema, context, "database.db")
        .also { schema.create(it).await() }
        .let(factory)
}
