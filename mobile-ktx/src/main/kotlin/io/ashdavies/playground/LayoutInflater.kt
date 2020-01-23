package io.ashdavies.playground

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> LayoutInflater.binding(
    @LayoutRes resId: Int,
    container: ViewGroup?,
    attachToParent: Boolean = container != null
): T = inflate(this, resId, container, attachToParent)
