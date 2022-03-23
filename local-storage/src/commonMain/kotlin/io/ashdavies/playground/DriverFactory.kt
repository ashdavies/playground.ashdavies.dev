package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.squareup.sqldelight.db.SqlDriver

public expect object DriverFactory {
    @Composable public operator fun invoke(schema: SqlDriver.Schema): SqlDriver
}
