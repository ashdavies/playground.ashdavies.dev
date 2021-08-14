package io.ashdavies.playground.view

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val OWNER_KEY = "io.ashdavies.playground.view.OWNER_KEY"

private val View.lifecycleOwner: LifecycleOwner
    get() = requireNotNull(ViewTreeLifecycleOwner.get(this)) { "View $this not attached to window" }

private val View.lifecycleScope: CoroutineScope
    get() = lifecycleOwner.lifecycleScope

@Suppress("UNCHECKED_CAST")
private inline fun <T> View.setTagIfAbsent(key: Int, block: () -> T): T =
    getTag(key) as? T ?: block()

private inline fun <T> View.setTagIfAbsent(key: String, block: () -> T): T =
    setTagIfAbsent(key.hashCode(), block)

private val View._lifecycleOwnerLiveData: LiveData<LifecycleOwner>
    get() = MutableLiveData<LifecycleOwner>().also { target ->
        onAttachStateChange(
            onAttachedToWindow = { target.value = it.lifecycleOwner },
            onDetachedFromWindow = { target.value = null }
        )
    }

val View.lifecycleOwnerLiveData: LiveData<LifecycleOwner>
    get() = setTagIfAbsent(OWNER_KEY) { _lifecycleOwnerLiveData }

fun View.launchWhenAttached(block: suspend CoroutineScope.() -> Unit) {
    onAttachStateChange(onAttachedToWindow = {
        lifecycleScope.launch { block() }
    })
}

private inline fun View.onAttachStateChange(
    crossinline onAttachedToWindow: (View) -> Unit,
    crossinline onDetachedFromWindow: () -> Unit = { },
) = object : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(view: View) = onAttachedToWindow(view)
    override fun onViewDetachedFromWindow(view: View) {
        removeOnAttachStateChangeListener(this)
        onDetachedFromWindow()
    }
}
