package io.ashdavies.playground

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

public actual class DriverConfig()

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlDriver.Schema,
        config: DriverConfig,
    ): SqlDriver = JdbcSqliteDriver(
        url = "${JdbcSqliteDriver.IN_MEMORY}database.db",
    )
}
