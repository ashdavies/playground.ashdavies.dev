package io.ashdavies.playground

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

public object DatabaseFactory {
    public operator fun <S : SqlDriver.Schema, T : Transacter> invoke(
        schema: S,
        config: DriverConfig,
        factory: (SqlDriver) -> T,
    ): T {
        val driver = DriverFactory(schema, config)
        val migration = DatabaseMigration(driver, schema)

        migration.migrate()
        return factory(driver)
    }
}
