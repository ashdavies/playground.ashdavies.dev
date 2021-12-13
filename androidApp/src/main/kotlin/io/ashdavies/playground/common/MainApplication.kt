package io.ashdavies.playground.common

import android.app.Application
import com.google.android.material.color.DynamicColors.applyToActivitiesIfAvailable

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        applyToActivitiesIfAvailable(this)
    }
}
