package io.ashdavies.playground.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DriverFactory(private val context: Context) {

    actual suspend fun create(): SqlDriver = AndroidSqliteDriver(
        schema = PlaygroundDatabase.Schema,
        name = "PlaygroundDatabase",
        context = context,
    )
}
