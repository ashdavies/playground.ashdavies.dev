package io.ashdavies.playground

import androidx.databinding.DataBindingUtil.getBinding
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

val Fragment.navController: NavController
  get() = findNavController()

fun <T : ViewDataBinding> Fragment.requireBinding(): T {
  return getBinding(requireView()) ?: throw IllegalStateException("Fragment $this has not been bound")
}
