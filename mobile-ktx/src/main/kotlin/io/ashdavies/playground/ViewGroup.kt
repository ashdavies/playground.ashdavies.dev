package io.ashdavies.playground

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.binding(
    @LayoutRes resId: Int,
    attachToParent: Boolean = true
): T = inflate(inflater, resId, this, attachToParent)
