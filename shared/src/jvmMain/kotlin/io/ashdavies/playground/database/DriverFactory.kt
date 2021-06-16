package io.ashdavies.playground.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY

actual class DriverFactory {
    actual fun create(): SqlDriver {
        return JdbcSqliteDriver(IN_MEMORY)
    }
}
