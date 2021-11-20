package io.ashdavies.notion

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.use
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private var SqlDriver.userVersion: Int by pragma { it.getIntOrDefault(default = 0) }

object DatabaseFactory {
    fun <S : SqlDriver.Schema, T : Transacter> create(schema: S, block: (SqlDriver) -> T): T {
        val driver: SqlDriver = DriverFactory.create()
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

private fun <T> pragma(block: (SqlCursor) -> T) = object : ReadWriteProperty<SqlDriver, T> {

    override fun getValue(thisRef: SqlDriver, property: KProperty<*>): T = thisRef
        .executeQuery(null, "PRAGMA ${camelToSnakeCase(property.name)};", 0, null)
        .use { block(it) }

    override fun setValue(thisRef: SqlDriver, property: KProperty<*>, value: T) {
        thisRef.execute(null, "PRAGMA ${camelToSnakeCase(property.name)} = $value;", 0, null)
    }
}

private fun camelToSnakeCase(input: String): String = input
    .split(Regex("(?=\\p{Upper})"))
    .joinToString("_") { it.lowercase() }

private fun SqlCursor.getIntOrDefault(index: Int = 0, default: Int): Int = getLong(index)
    ?.toInt()
    ?: default
