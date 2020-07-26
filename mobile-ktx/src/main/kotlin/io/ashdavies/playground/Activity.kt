package io.ashdavies.playground

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
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

fun <T : ViewBinding> FragmentActivity.viewBinding(
    bindingProducer: (LayoutInflater) -> T
): Lazy<T> = ViewBindingLazy(
    bindingProducer = bindingProducer,
    ownerProducer = { this },
    inflaterProducer = { layoutInflater }
)
