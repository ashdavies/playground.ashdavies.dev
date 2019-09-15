package io.ashdavies.databinding.repos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import io.ashdavies.databinding.R
import io.ashdavies.databinding.databinding.ActivityRepoBinding
import io.ashdavies.databinding.extensions.activityBinding
import io.ashdavies.databinding.extensions.setOnQueryTextChanged
import io.ashdavies.databinding.extensions.snack
import io.ashdavies.databinding.models.Repo
import io.ashdavies.lifecycle.EventObserver
import kotlinx.coroutines.FlowPreview
import kotlin.LazyThreadSafetyMode.NONE

@FlowPreview
internal class RepoActivity : AppCompatActivity() {

  private val binding: ActivityRepoBinding by activityBinding(R.layout.activity_repo)
  private val coordinator: CoordinatorLayout by lazy(NONE) { binding.coordinator }
  private val model: RepoViewModel by viewModels { RepoViewModel.Factory(this) }

  private val adapter: RepoAdapter<Repo> = RepoAdapter(R.layout.list_item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    with(binding) {
      setSupportActionBar(toolbar)

      lifecycleOwner = this@RepoActivity
      model = model

      recycler.adapter = adapter
      search.setOnQueryTextChanged(this@RepoActivity.model::onQuery)
    }

    model
        .items
        .observe(this, Observer(adapter::submitList))

    model
        .error
        .observe(this, EventObserver(::error))

    model.onQuery("Kotlin")
  }

  private fun error(throwable: Throwable) {
    val message: String? = throwable.message
    if (message == null) {
      coordinator.snack(R.string.unexpected_error)
      return
    }

    coordinator.snack(message)
  }
}
