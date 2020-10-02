package io.ashdavies.playground.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun create(): SqlDriver = NativeSqliteDriver(
        PlaygroundDatabase::class.simpleName,
        PlaygroundDatabase.Schema,
        "PlaygroundDatabase",
    )
}
