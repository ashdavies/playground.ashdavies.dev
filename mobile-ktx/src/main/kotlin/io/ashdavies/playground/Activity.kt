package io.ashdavies.playground

import android.app.Activity
import android.view.View
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.NONE

val Activity.rootView: View
    get() = window
        .decorView
        .rootView

inline fun <T : ViewBinding> Activity.viewBinding(
    crossinline block: (View) -> T
): Lazy<T> = lazy(NONE) { block(rootView) }
