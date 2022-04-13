package io.ashdavies.playground

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.use

private const val VersionPragma = "user_version"

internal class DatabaseMigration(private val driver: SqlDriver, private val schema: SqlDriver.Schema) {

    private var SqlDriver.oldVersion: Int
        set(value) = execute(null, "PRAGMA $VersionPragma = $value", 0)
        get() = executeQuery(null, "PRAGMA $VersionPragma;", 0).use {
            (if (it.next()) it.getLong(0)?.toInt() else null) ?: 0
        }

    fun migrate(oldVersion: Int = driver.oldVersion, newVersion: Int = schema.version) {
        driver.oldVersion = when (oldVersion) {
            0 -> run { schema.create(driver); 1 }
            else -> {
                schema.migrate(driver, oldVersion, newVersion)
                schema.version
            }
        }
    }
}
