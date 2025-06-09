package io.ashdavies.sql

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver = createDefaultWebWorkerDriver()
}
