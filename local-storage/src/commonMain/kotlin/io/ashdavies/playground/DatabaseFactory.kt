package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

@Composable
public fun <T : Transacter> rememberDatabase(
    schema: SqlDriver.Schema,
    factory: (SqlDriver) -> T,
): T {
    val config = getDriverConfig()
    return remember(schema, config) {
        DatabaseFactory(
            factory = factory,
            schema = schema,
            config = config,
        )
    }
}
