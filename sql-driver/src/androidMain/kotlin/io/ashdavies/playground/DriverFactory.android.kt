package io.ashdavies.playground

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

public actual class DriverConfig(
    public val context: Context,
)

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        config: DriverConfig,
    ): SqlDriver = AndroidSqliteDriver(
        context = config.context,
        schema = schema,
    )
}
