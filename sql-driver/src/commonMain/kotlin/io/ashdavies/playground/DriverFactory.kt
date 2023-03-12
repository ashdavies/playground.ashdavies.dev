package io.ashdavies.playground

import com.squareup.sqldelight.db.SqlDriver

public expect class DriverConfig

public expect object DriverFactory {
    public operator fun invoke(
        schema: SqlDriver.Schema,
        config: DriverConfig,
    ): SqlDriver
}
