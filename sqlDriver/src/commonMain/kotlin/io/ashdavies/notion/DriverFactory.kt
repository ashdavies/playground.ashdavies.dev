package io.ashdavies.notion

import com.squareup.sqldelight.db.SqlDriver

expect object DriverFactory {
    fun create(): SqlDriver
}
