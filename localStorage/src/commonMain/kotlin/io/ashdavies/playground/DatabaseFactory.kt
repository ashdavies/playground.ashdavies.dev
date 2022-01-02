package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

object DatabaseFactory {
    @Composable operator fun <S : SqlDriver.Schema, T : Transacter> invoke(schema: S, block: (SqlDriver) -> T): T {
        return block(DriverFactory(schema))
    }
}
