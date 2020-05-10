package io.ashdavies.playground

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import kotlin.LazyThreadSafetyMode.NONE

fun <T : ViewDataBinding> Activity.binding(
    @LayoutRes resId: Int
): Lazy<T> = lazy(NONE) {
    setContentView<T>(this, resId)
}
