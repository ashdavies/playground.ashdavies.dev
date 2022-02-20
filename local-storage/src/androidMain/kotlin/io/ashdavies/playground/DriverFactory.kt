package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual object DriverFactory {
    @Composable actual operator fun invoke(schema: SqlDriver.Schema): SqlDriver {
        return AndroidSqliteDriver(schema, LocalContext.current)
    }
}
