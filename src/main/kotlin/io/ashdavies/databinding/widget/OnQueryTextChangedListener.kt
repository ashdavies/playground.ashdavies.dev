package io.ashdavies.databinding.widget

import androidx.appcompat.widget.SearchView

internal class OnQueryTextChangedListener(private val block: (String) -> Unit) : SearchView.OnQueryTextListener {

  override fun onQueryTextSubmit(value: String): Boolean {
    return true
  }

  override fun onQueryTextChange(value: String): Boolean {
    block(value)
    return true
  }
}
