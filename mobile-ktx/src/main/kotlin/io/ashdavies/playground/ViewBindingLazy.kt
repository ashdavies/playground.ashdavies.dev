package io.ashdavies.playground

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

internal class ViewBindingLazy<T : ViewBinding>(
    private val bindingProducer: (View) -> T,
    private val ownerProducer: () -> LifecycleOwner,
    private val viewProducer: () -> View
) : Lazy<T> {

    private var cached: T? = null

    override val value: T
        get() = cached ?: run {
            val lifecycleOwner: LifecycleOwner = ownerProducer()
            lifecycleOwner.onDestroy {
                cached = null
            }

            val view: View = viewProducer()
            val binding: T = bindingProducer(view)

            cached = binding
            return binding
        }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}
