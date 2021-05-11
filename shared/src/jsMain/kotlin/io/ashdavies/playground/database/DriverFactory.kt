package io.ashdavies.playground.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await

actual class DriverFactory {

    actual suspend fun create(): SqlDriver {
        return initSqlDriver(PlaygroundDatabase.Schema).await()
    }
}
