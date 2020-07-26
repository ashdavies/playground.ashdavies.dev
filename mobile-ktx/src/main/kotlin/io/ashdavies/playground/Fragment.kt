package io.ashdavies.playground

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlin.LazyThreadSafetyMode.NONE

fun Fragment.navController(): Lazy<NavController> = lazy(NONE) {
    findNavController()
}

fun <T : ViewBinding> Fragment.viewBinding(
    bindingProducer: (LayoutInflater) -> T
): Lazy<T> = ViewBindingLazy(
    bindingProducer = bindingProducer,
    ownerProducer = { viewLifecycleOwner },
    inflaterProducer = { layoutInflater }
)
