package io.ashdavies.databinding.databinding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:onQueryTextChange")
internal fun setOnQueryTextListener(view: SearchView, listener: OnQueryTextChange) {
  view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(it: String): Boolean = true
    override fun onQueryTextChange(it: String): Boolean = listener(it)
  })
}

internal interface OnQueryTextChange {

  operator fun invoke(string: String): Boolean
}
