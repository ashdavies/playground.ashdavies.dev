package io.ashdavies.playground

import com.squareup.sqldelight.db.SqlDriver

internal class DatabaseMigration(private val driver: SqlDriver, private val schema: SqlDriver.Schema) {

    private var SqlDriver.userVersion: Int
        set(value) = execute(null, "PRAGMA user_version = $value", 0, null)
        get() = executeQuery(null, "PRAGMA user_version;", 0, null)
            .takeIf { it.next() }
            ?.getLong(0)
            ?.toInt()
            ?: 0

    fun migrate() {
        val userVersion = driver.userVersion

        if (userVersion == 0) {
            driver.userVersion = 1
            schema.create(driver)
        } else if (userVersion < schema.version) {
            schema.migrate(driver, userVersion, schema.version)
            driver.userVersion = schema.version
        }
    }
}
