package io.ashdavies.playground

import androidx.appcompat.widget.SearchView

fun SearchView.setOnQueryTextChanged(block: (String) -> Unit) = setOnQueryTextListener(object : SearchView.OnQueryTextListener {

  override fun onQueryTextSubmit(value: String): Boolean {
    return true
  }

  override fun onQueryTextChange(value: String): Boolean {
    block(value)
    return true
  }
})
