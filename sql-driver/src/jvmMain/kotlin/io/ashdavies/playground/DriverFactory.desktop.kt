package io.ashdavies.playground

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

public actual class DriverConfig()

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlSchema<QueryResult.Value<Unit>>,
        config: DriverConfig,
    ): SqlDriver = JdbcSqliteDriver(
        url = "${JdbcSqliteDriver.IN_MEMORY}database.db",
    )
}
