package io.ashdavies.playground.conferences

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import io.ashdavies.playground.models.Repo

internal object ConferencesComparator : ItemCallback<Repo>() {

  override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem

  override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
}
