package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.squareup.sqldelight.db.SqlDriver

expect object DriverFactory {
    @Composable operator fun invoke(schema: SqlDriver.Schema): SqlDriver
}
