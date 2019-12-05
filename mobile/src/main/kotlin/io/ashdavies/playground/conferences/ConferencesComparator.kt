package io.ashdavies.playground.conferences

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import io.ashdavies.playground.network.Conference

internal object ConferencesComparator : ItemCallback<Conference>() {

  override fun areContentsTheSame(oldItem: Conference, newItem: Conference): Boolean = oldItem == newItem

  override fun areItemsTheSame(oldItem: Conference, newItem: Conference): Boolean = oldItem == newItem
}
