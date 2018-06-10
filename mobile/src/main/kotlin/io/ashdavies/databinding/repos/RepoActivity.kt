package io.ashdavies.databinding.repos

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import io.ashdavies.databinding.R
import io.ashdavies.databinding.common.BaseActivity
import io.ashdavies.databinding.common.SingleLayoutAdapter
import io.ashdavies.databinding.extensions.getViewModel
import io.ashdavies.databinding.extensions.itemDecorations
import io.ashdavies.databinding.models.Repo
import kotlinx.android.synthetic.main.activity_main.recycler

internal class RepoActivity : BaseActivity(), SearchView.OnQueryTextListener {

  private lateinit var viewModel: RepoViewModel
  private lateinit var adapter: SingleLayoutAdapter<Repo>
  private lateinit var search: SearchView

  override val layoutResId: Int = R.layout.activity_main
  override val toolbarId: Int = R.id.toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    adapter = SingleLayoutAdapter(R.layout.list_item)

    recycler.adapter = adapter
    recycler.setHasFixedSize(true)
    recycler.layoutManager = LinearLayoutManager(this)
    recycler.itemDecorations += DividerItemDecoration(this, VERTICAL)

    viewModel = getViewModel(RepoViewModel.Factory())
    viewModel.items.observe(this, Observer { adapter.items = it!! })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)

    val item = menu.findItem(R.id.action_search)
    search = item.actionView as SearchView
    search.setOnQueryTextListener(this)
    search.onActionViewExpanded()

    return true
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    viewModel.getRepos(query)
    search.clearFocus()
    return true
  }

  override fun onQueryTextChange(query: String): Boolean {
    return true
  }
}
