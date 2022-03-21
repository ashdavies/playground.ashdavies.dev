package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

public actual object DriverFactory {

    @Composable
    public actual operator fun invoke(schema: SqlDriver.Schema): SqlDriver {
        return JdbcSqliteDriver("${JdbcSqliteDriver.IN_MEMORY}database.db")
    }
}
