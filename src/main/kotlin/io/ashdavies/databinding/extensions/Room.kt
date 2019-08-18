package io.ashdavies.databinding.extensions

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

internal inline fun <reified T : RoomDatabase> databaseBuilder(application: Application, name: String) = Room.databaseBuilder(application, T::class.java, name)
