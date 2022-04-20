package io.ashdavies.playground

import android.app.Application
import com.google.android.material.color.DynamicColors.applyToActivitiesIfAvailable

public abstract class DynamicColorsApplication : Application() {
    override fun onCreate() {
        applyToActivitiesIfAvailable(this)
        super.onCreate();
    }
}
