package io.ashdavies.databinding.extensions

import android.app.Application
import androidx.room.RoomDatabase

internal inline fun <reified T : RoomDatabase> Application.database(name: String) = databaseBuilder<T>(this, name).build()
