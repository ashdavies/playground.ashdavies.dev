package io.ashdavies.databinding.extensions

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import io.ashdavies.databinding.common.ActivityBindingProperty

internal fun <T : ViewDataBinding> activityBinding(@LayoutRes resId: Int) = ActivityBindingProperty<T>(resId)
