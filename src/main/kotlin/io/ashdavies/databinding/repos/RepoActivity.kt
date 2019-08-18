package io.ashdavies.databinding.repos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import io.ashdavies.databinding.R
import io.ashdavies.databinding.databinding.ActivityRepoBinding
import io.ashdavies.databinding.extensions.activityBinding
import io.ashdavies.databinding.extensions.snack
import io.ashdavies.databinding.models.Repo
import io.ashdavies.lifecycle.EventObserver
import kotlinx.coroutines.FlowPreview

@FlowPreview
internal class RepoActivity : AppCompatActivity() {

  private val binding: ActivityRepoBinding by activityBinding(R.layout.activity_repo)
  private val model: RepoViewModel by viewModels { RepoViewModel.Factory(this) }

  private val adapter: RepoAdapter<Repo> = RepoAdapter(R.layout.list_item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    with(binding) {
      setSupportActionBar(toolbar)

      lifecycleOwner = this@RepoActivity
      model = model

      recycler.addItemDecoration(DividerItemDecoration(this@RepoActivity, VERTICAL))
      recycler.adapter = adapter
    }

    model
        .items
        .observe(this, Observer(adapter::submitList))

    model
        .error
        .observe(this, EventObserver(::error))

    model.onQuery("ashdavies")
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
