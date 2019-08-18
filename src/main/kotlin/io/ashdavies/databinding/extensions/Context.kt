package io.ashdavies.databinding.extensions

import android.app.Application
import android.content.Context
import androidx.room.RoomDatabase

internal inline fun <reified T : RoomDatabase> Context.database(name: String): T = applicationContext
    .unsafeCast<Application>()
    .database(name)
