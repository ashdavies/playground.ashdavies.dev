package io.ashdavies.playground

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(
    bindingProducer: (View) -> T
): Lazy<T> = ViewBindingLazy(
    bindingProducer = bindingProducer,
    ownerProducer = { viewLifecycleOwner },
    viewProducer = { requireView() }
)
