package io.ashdavies.playground.database

import com.squareup.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun create(): SqlDriver
}
