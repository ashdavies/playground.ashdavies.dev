package io.ashdavies.playground

import android.app.Application
import com.google.android.material.color.DynamicColors.applyToActivitiesIfAvailable

internal class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        applyToActivitiesIfAvailable(this)
    }
}
