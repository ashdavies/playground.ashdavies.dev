package io.ashdavies.databinding.repos

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.ashdavies.databinding.repos.RepoAdapter.ViewHolder
import io.ashdavies.databinding.databinding.ListItemBinding
import io.ashdavies.databinding.extensions.binding
import io.ashdavies.databinding.models.Repo

internal class RepoAdapter<T>(@LayoutRes private val resId: Int) : ListAdapter<Repo, ViewHolder>(RepoItemCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder = ViewHolder(parent.binding(resId, false))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

  class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo) {
      binding.item = item
    }
  }
}
