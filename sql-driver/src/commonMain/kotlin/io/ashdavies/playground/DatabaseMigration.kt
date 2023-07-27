package io.ashdavies.playground

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

private const val VersionPragma = "user_version"

internal class DatabaseMigration(
    private val driver: SqlDriver,
    private val schema: SqlSchema<QueryResult.Value<Unit>>,
) {

    private var SqlDriver.oldVersion: Long
        get() = executeQuery(
            identifier = null,
            sql = "PRAGMA $VersionPragma;",
            mapper = { QueryResult.Value(it.nextOrNull { getLong(0) } ?: 0L) },
            parameters = 0,
            binders = null,
        ).value
        set(value) {
            execute(
                identifier = null,
                sql = "PRAGMA $VersionPragma = $value",
                parameters = 0,
            )
        }

    fun migrate(
        oldVersion: Long = driver.oldVersion,
        newVersion: Long = schema.version,
    ) {
        driver.oldVersion = when (oldVersion) {
            0L -> run { schema.create(driver); 1 }
            else -> {
                schema.migrate(driver, oldVersion, newVersion)
                schema.version
            }
        }
    }
}

private fun <T : Any> SqlCursor.nextOrNull(block: SqlCursor.() -> T?): T? {
    return if (next().value) block() else null
}
