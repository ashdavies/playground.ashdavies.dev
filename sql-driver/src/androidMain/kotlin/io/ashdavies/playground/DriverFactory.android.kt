package io.ashdavies.playground

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.ashdavies.content.PlatformContext

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver = AndroidSqliteDriver(
        schema = schema,
        context = context,
        name = name,
    )
}
