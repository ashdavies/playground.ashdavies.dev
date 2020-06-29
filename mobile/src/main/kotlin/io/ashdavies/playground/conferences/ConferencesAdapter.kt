package io.ashdavies.playground.conferences

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.ashdavies.playground.conferences.ConferencesAdapter.ViewHolder
import io.ashdavies.playground.databinding.ListItemBinding
import io.ashdavies.playground.databinding.ListItemBinding.inflate
import io.ashdavies.playground.layoutInflater
import io.ashdavies.playground.network.Conference
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

private val DateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)

internal class ConferencesAdapter : PagedListAdapter<Conference, ViewHolder>(
    diffCallback = ConferencesComparator
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        type: Int
    ): ViewHolder = ViewHolder(inflate(parent.layoutInflater))

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ): Unit = holder.bind(getItem(position))

    class ViewHolder(
        private val viewBinding: ListItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: Conference?): Unit = with(viewBinding) {
            name.text = item?.name
            website.text = item?.website
            country.text = item?.country
            city.text = item?.city

            dateStart.text = item
                ?.dateStart
                ?.let(DateFormat::format)
        }
    }
}
