package io.ashdavies.databinding.extensions

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding

internal fun <T : ViewDataBinding> ViewGroup.binding(@LayoutRes resId: Int, attachToParent: Boolean = true): T {
  return inflate<T>(inflater, resId, this, attachToParent)
}
