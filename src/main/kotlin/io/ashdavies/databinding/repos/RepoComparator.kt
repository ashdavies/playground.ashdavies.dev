package io.ashdavies.databinding.repos

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import io.ashdavies.databinding.models.Repo

internal object RepoComparator : ItemCallback<Repo>() {

  override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem

  override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
}
