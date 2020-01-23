package io.ashdavies.playground

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

inline fun <reified T : RoomDatabase> databaseBuilder(
    context: Context,
    name: String
): RoomDatabase.Builder<T> = databaseBuilder(context, T::class.java, name)
