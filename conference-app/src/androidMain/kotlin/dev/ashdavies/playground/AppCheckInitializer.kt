package dev.ashdavies.playground

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.appcheck.FirebaseAppCheck

internal class AppCheckInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val appCheck = FirebaseAppCheck.getInstance()
        val factory = AppCheckProviderFactory()

        appCheck.installAppCheckProviderFactory(factory)
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>?>?>()
}
