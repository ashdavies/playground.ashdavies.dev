package io.ashdavies.playground

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(
    bindingProducer: (LayoutInflater) -> T
): Lazy<T> = ViewBindingLazy(
    bindingProducer = bindingProducer,
    ownerProducer = { viewLifecycleOwner },
    inflaterProducer = { layoutInflater }
)
