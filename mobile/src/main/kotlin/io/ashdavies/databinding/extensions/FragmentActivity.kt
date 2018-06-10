package io.ashdavies.databinding.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

internal inline fun <reified T : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory = ViewModelProvider.NewInstanceFactory()): T {
  return ViewModelProviders.of(this, factory).get(T::class.java)
}
