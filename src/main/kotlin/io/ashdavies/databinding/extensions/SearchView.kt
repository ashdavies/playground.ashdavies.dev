package io.ashdavies.databinding.extensions

import androidx.appcompat.widget.SearchView
import io.ashdavies.databinding.widget.OnQueryTextChangedListener

internal fun SearchView.setOnQueryTextChanged(block: (String) -> Unit) = setOnQueryTextListener(OnQueryTextChangedListener(block))
