package dev.ashdavies.sql

import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public interface DatabaseFactory<T : SuspendingTransacter> : Suspended<T>

public fun <T : SuspendingTransacter> DatabaseFactory(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    context: PlatformContext,
    factory: suspend (SqlDriver) -> T,
): DatabaseFactory<T> = object :
    DatabaseFactory<T>,
    Suspended<T> by Suspended(
        initializer = {
            DriverFactory(
                schema = schema,
                context = context,
                name = "database.db",
            ).let { factory(it) }
        },
    ) { }
