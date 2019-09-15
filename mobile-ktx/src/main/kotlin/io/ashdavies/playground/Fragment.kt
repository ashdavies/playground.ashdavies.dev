package io.ashdavies.playground

import androidx.databinding.DataBindingUtil.getBinding
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.requireBinding(): T = getBinding(requireView()) ?: throw IllegalStateException("Fragment $this has not been bound")
