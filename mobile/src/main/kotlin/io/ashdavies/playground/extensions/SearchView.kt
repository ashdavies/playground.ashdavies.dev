package io.ashdavies.playground.extensions

import androidx.appcompat.widget.SearchView
import io.ashdavies.playground.widget.OnQueryTextChangedListener

internal fun SearchView.setOnQueryTextChanged(block: (String) -> Unit) = setOnQueryTextListener(OnQueryTextChangedListener(block))
