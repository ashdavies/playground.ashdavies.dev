package io.ashdavies.playground.ktx

import android.content.Context
import androidx.room.RoomDatabase

inline fun <reified T : RoomDatabase> Context.database(name: String): T {
    return databaseBuilder<T>(this, name).build()
}
