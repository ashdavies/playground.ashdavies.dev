package io.ashdavies.playground

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.ashdavies.content.PlatformContext

public object DatabaseFactory {
    public operator fun <S : SqlSchema<QueryResult.Value<Unit>>, T : Transacter> invoke(
        schema: S,
        context: PlatformContext,
        factory: (SqlDriver) -> T,
    ): T {
        val driver = DriverFactory(schema, context)
        val migration = DatabaseMigration(driver, schema)

        migration.migrate()
        return factory(driver)
    }
}
