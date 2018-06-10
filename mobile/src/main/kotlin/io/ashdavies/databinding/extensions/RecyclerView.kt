package io.ashdavies.databinding.extensions

import androidx.recyclerview.widget.RecyclerView

internal val RecyclerView.itemDecorations: ItemDecorations get() = ItemDecorations(this)

internal class ItemDecorations(private val view: RecyclerView) {

  operator fun plusAssign(decoration: RecyclerView.ItemDecoration) = view.addItemDecoration(decoration)
}
