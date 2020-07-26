package io.ashdavies.playground

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

internal class ViewBindingLazy<T : ViewBinding>(
    private val bindingProducer: (LayoutInflater) -> T,
    private val ownerProducer: () -> LifecycleOwner,
    private val inflaterProducer: () -> LayoutInflater
) : Lazy<T> {

    private var cached: T? = null

    override val value: T
        get() = cached ?: run {
            val lifecycleOwner: LifecycleOwner = ownerProducer()
            lifecycleOwner.onDestroy {
                cached = null
            }

            val inflater: LayoutInflater = inflaterProducer()
            val binding: T = bindingProducer(inflater)

            cached = binding
            return binding
        }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}
