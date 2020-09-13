package io.ashdavies.playground

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlin.LazyThreadSafetyMode.NONE

val Activity.rootView: View
    get() = window
        .decorView
        .rootView

fun Activity.navController(
    @IdRes viewId: Int
): Lazy<NavController> = lazy(NONE) {
    findNavController(viewId)
}
