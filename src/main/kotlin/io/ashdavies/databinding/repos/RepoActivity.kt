package io.ashdavies.databinding.repos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import io.ashdavies.databinding.R
import io.ashdavies.databinding.common.SingleLayoutAdapter
import io.ashdavies.databinding.databinding.ActivityRepoBinding
import io.ashdavies.databinding.extensions.activityBinding
import io.ashdavies.databinding.extensions.itemDecorations
import io.ashdavies.databinding.extensions.snack
import io.ashdavies.databinding.models.Repo
import io.ashdavies.lifecycle.EventObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal class RepoActivity : AppCompatActivity() {

  private val binding: ActivityRepoBinding by activityBinding(R.layout.activity_repo)
  private val model: RepoViewModel by viewModels { RepoViewModel.Factory() }

  private val adapter: SingleLayoutAdapter<Repo> = SingleLayoutAdapter(R.layout.list_item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.model = model
    binding.lifecycleOwner = this
    setSupportActionBar(binding.toolbar)

    with(binding) {
      recycler.adapter = adapter
      recycler.setHasFixedSize(true)
      recycler.layoutManager = LinearLayoutManager(this@RepoActivity)
      recycler.itemDecorations += DividerItemDecoration(this@RepoActivity, VERTICAL)
    }

    model
        .items
        .observe(this, Observer { adapter.items = it })

    model
        .error
        .observe(this, EventObserver(::error))
  }

  private fun error(throwable: Throwable) {
    with(binding) {
      val message: String? = throwable.message
      if (message == null) {
        coordinator.snack(R.string.unexpected_error)
        return
      }

      coordinator.snack(message)
    }
  }
}
