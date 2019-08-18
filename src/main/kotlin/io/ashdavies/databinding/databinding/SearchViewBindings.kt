package io.ashdavies.databinding.databinding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

@BindingAdapter("onQueryTextChange")
internal fun setOnQueryTextListener(view: SearchView, listener: OnQueryTextChange?) {
  if (listener == null) {
    view.setOnQueryTextListener(null)
    return
  }

  view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
    override fun onQueryTextChange(it: String): Boolean {
      listener(it)
      return true
    }

    override fun onQueryTextSubmit(it: String): Boolean = true
  })
}

internal interface OnQueryTextChange {

  operator fun invoke(string: String)
}
