package io.ashdavies.notion

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY

private const val DATABASE_URL = "database.db"

actual object DriverFactory {
    actual fun create(): SqlDriver = JdbcSqliteDriver("$IN_MEMORY$DATABASE_URL")
}
