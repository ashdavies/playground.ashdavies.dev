package io.ashdavies.sql

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import io.ashdavies.content.PlatformContext

public actual object DriverFactory {
    public actual suspend operator fun invoke(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
        context: PlatformContext,
        name: String,
    ): SqlDriver = createDefaultWebWorkerDriver().also {
        schema.awaitCreate(it)
    }
}
