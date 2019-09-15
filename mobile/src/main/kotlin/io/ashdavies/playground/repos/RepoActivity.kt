package io.ashdavies.playground.repos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import io.ashdavies.lifecycle.EventObserver
import io.ashdavies.playground.R
import io.ashdavies.playground.binding
import io.ashdavies.playground.databinding.ActivityRepoBinding
import io.ashdavies.playground.extensions.setOnQueryTextChanged
import io.ashdavies.playground.models.Repo
import io.ashdavies.playground.snack
import kotlinx.coroutines.FlowPreview
import kotlin.LazyThreadSafetyMode.NONE

@FlowPreview
internal class RepoActivity : AppCompatActivity() {

  private val binding: ActivityRepoBinding by binding(R.layout.activity_repo)
  private val coordinator: CoordinatorLayout by lazy(NONE) { binding.coordinator }
  private val model: RepoViewModel by viewModels { RepoViewModel.Factory(this) }

  private val adapter: RepoAdapter<Repo> = RepoAdapter(R.layout.list_item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(binding.toolbar)
    binding.model = model

    binding
        .recycler
        .adapter = adapter

    binding
        .search
        .setOnQueryTextChanged(model::onQuery)

    model
        .items
        .observe(this, Observer(adapter::submitList))

    model
        .error
        .observe(this, EventObserver(::error))
  }

  private fun error(throwable: Throwable) = throwable
      .message
      ?.let { coordinator.snack(it) }
      ?: coordinator.snack(R.string.unexpected_error)
}
