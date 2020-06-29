package io.ashdavies.playground

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

val Activity.rootView: View
    get() = window
        .decorView
        .rootView

fun <T : ViewBinding> FragmentActivity.viewBinding(
    bindingProducer: (View) -> T
): Lazy<T> = ViewBindingLazy(
    bindingProducer = bindingProducer,
    ownerProducer = { this },
    viewProducer = { rootView }
)
