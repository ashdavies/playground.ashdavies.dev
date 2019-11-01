package io.ashdavies.playground.conferences

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.ashdavies.playground.binding
import io.ashdavies.playground.databinding.ListItemBinding
import io.ashdavies.playground.models.Repo
import io.ashdavies.playground.conferences.ConferencesAdapter.ViewHolder

internal class ConferencesAdapter<T>(
    @LayoutRes private val resId: Int
) : PagedListAdapter<Repo, ViewHolder>(ConferencesComparator) {

  override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
    return ViewHolder(parent.binding(resId, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    return holder.bind(getItem(position))
  }

  class ViewHolder(
      private val binding: ListItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo?) {
      binding.item = item
    }
  }
}
