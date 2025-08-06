package dev.ashdavies.sql

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.ashdavies.content.PlatformContext

public actual object DriverFactory {
    public actual suspend operator fun invoke(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver = JdbcSqliteDriver(
        url = "${JdbcSqliteDriver.IN_MEMORY}$name",
        schema = schema.synchronous(),
    )
}
