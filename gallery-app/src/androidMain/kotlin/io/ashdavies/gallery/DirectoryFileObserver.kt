package io.ashdavies.gallery

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.FileObserver

internal fun observeFileDirectory(context: Context, file: Uri, onCreate: (String) -> Unit): ObservationResult {
    val callbacks = FileActivityCallbacks(DirectoryFileObserver(file.toString(), onCreate))
    val application = context.applicationContext as Application
    application.registerActivityLifecycleCallbacks(callbacks)
    println("observeFileDirectory.registerActivityLifecycleCallbacks($file)")

    return ObservationResult {
        println("observeFileDirectory.unregisterActivityLifecycleCallbacks")
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }
}

private class FileActivityCallbacks(private val observer: DirectoryFileObserver) : DefaultActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = observer.startWatching()
    override fun onActivityDestroyed(activity: Activity) = observer.stopWatching()
}

private class DirectoryFileObserver(path: String, private val onCreate: (String) -> Unit) : FileObserver(path) {
    override fun onEvent(event: Int, path: String?) = if (path != null) onCreate(path) else Unit
}

private interface DefaultActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

internal fun interface ObservationResult {
    fun cancel()
}
