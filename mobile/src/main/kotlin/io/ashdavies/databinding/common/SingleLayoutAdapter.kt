package io.ashdavies.databinding.common

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.ashdavies.databinding.BR
import io.ashdavies.databinding.extensions.inflater
import kotlin.properties.Delegates.observable

internal class SingleLayoutAdapter<T>(@LayoutRes private val resId: Int) : RecyclerView.Adapter<SingleLayoutAdapter.ViewHolder<T>>() {

  var items by observable<List<T>>(listOf()) { _, oldValue, newValue ->
    DiffUtil
        .calculateDiff(DiffUtilCallback(oldValue, newValue))
        .dispatchUpdatesTo(this)
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) = holder.bind(items[position])

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder<T>(parent.inflate())

  private fun ViewGroup.inflate() = DataBindingUtil.inflate<ViewDataBinding>(inflater, resId, this, false)

  class ViewHolder<in T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) = with(binding) {
      setVariable(BR.item, item)
      executePendingBindings()
    }
  }
}
