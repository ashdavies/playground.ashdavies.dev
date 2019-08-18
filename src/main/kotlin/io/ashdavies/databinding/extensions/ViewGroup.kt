package io.ashdavies.databinding.extensions

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding

internal fun ViewGroup.binding(@LayoutRes resId: Int, attachToParent: Boolean = true) = inflate<ViewDataBinding>(inflater, resId, this, attachToParent)
