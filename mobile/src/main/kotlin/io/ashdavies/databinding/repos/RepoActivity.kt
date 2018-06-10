package io.ashdavies.databinding.repos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import io.ashdavies.databinding.R
import io.ashdavies.databinding.common.NotNullObserver
import io.ashdavies.databinding.common.SingleLayoutAdapter
import io.ashdavies.databinding.databinding.ActivityRepoBinding
import io.ashdavies.databinding.extensions.activityBinding
import io.ashdavies.databinding.extensions.getViewModel
import io.ashdavies.databinding.extensions.itemDecorations
import io.ashdavies.databinding.extensions.snack
import io.ashdavies.databinding.models.Repo
import kotlinx.android.synthetic.main.activity_repo.coordinator
import kotlinx.android.synthetic.main.activity_repo.recycler
import kotlinx.android.synthetic.main.activity_repo.search
import kotlinx.android.synthetic.main.activity_repo.toolbar

internal class RepoActivity : AppCompatActivity() {

  private val binding: ActivityRepoBinding by activityBinding(R.layout.activity_repo)
  private val model: RepoViewModel by lazy { getViewModel<RepoViewModel>(RepoViewModel.Factory()) }

  private val adapter: SingleLayoutAdapter<Repo> = SingleLayoutAdapter(R.layout.list_item)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.model = model
    binding.setLifecycleOwner(this)
    setSupportActionBar(toolbar)

    recycler.adapter = adapter
    recycler.setHasFixedSize(true)
    recycler.layoutManager = LinearLayoutManager(this)
    recycler.itemDecorations += DividerItemDecoration(this, VERTICAL)

    search.onActionViewExpanded()

    model.items.observe(this, NotNullObserver { adapter.items = it })
    model.error.observe(this, NotNullObserver(::error))
  }

  private fun error(throwable: Throwable) {
    val message = throwable.message
    if (message == null) {
      coordinator.snack(R.string.unexpected_error)
      return
    }

    coordinator.snack(message)
  }
}
