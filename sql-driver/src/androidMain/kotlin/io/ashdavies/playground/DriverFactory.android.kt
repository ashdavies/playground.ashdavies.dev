package io.ashdavies.playground

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

public actual class DriverConfig(
    public val context: Context,
)

public actual object DriverFactory {
    public actual operator fun invoke(
        schema: SqlDriver.Schema,
        config: DriverConfig,
    ): SqlDriver = AndroidSqliteDriver(
        context = config.context,
        schema = schema,
    )
}
