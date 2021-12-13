package io.ashdavies.playground

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver

private var SqlDriver.userVersion: Int
    set(value) = execute(null, "PRAGMA user_version = $value", 0, null)
    get() = executeQuery(null, "PRAGMA user_version;", 0, null)
        .getIntOrNull()
        ?: 0

class DatabaseFactory(private val factory: suspend (SqlDriver.Schema) -> SqlDriver) {
    suspend fun <S : SqlDriver.Schema, T : Transacter> create(schema: S, block: (SqlDriver) -> T): T {
        val driver: SqlDriver = factory(schema)
        val version: Int = driver.userVersion

        if (version == 0) {
            driver.userVersion = 1
            schema.create(driver)
        } else if (version < schema.version) {
            schema.migrate(driver, version, schema.version)
            driver.userVersion = schema.version
        }

        return block(driver)
    }
}

private fun SqlCursor.getIntOrNull(): Int? = takeIf { next() }
    ?.getLong(0)
    ?.toInt()
