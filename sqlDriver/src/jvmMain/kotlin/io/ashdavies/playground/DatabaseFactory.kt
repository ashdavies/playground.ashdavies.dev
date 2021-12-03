package io.ashdavies.playground

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY

private const val DATABASE_URL = "database.db"

fun DatabaseFactory() = DatabaseFactory {
    JdbcSqliteDriver("$IN_MEMORY$DATABASE_URL")
}
