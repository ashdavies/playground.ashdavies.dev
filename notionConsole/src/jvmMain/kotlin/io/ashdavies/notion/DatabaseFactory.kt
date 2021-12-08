package io.ashdavies.notion

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ashdavies.playground.DatabaseFactory

private const val DATABASE_URL = "database.db"

actual fun DatabaseFactory() = DatabaseFactory {
    JdbcSqliteDriver("${JdbcSqliteDriver.IN_MEMORY}$DATABASE_URL")
}
