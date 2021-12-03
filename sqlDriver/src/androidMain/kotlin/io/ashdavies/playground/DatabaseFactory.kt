package io.ashdavies.playground

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver

fun DatabaseFactory(context: Context) = DatabaseFactory {
    AndroidSqliteDriver(it, context)
}
