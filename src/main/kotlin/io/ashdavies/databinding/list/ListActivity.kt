package io.ashdavies.databinding.list

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.ashdavies.databinding.R
import io.ashdavies.databinding.databinding.ActivityListBinding
import io.ashdavies.databinding.extensions.activityBinding
import io.ashdavies.databinding.repos.retrofit
import io.ashdavies.databinding.services.GitHubService
import kotlinx.coroutines.launch
import retrofit2.create

internal class ListActivity : AppCompatActivity() {

  private val binding: ActivityListBinding by activityBinding(R.layout.activity_list)

  private val service: GitHubService = retrofit.create()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(binding.toolbar)
    binding.lifecycleOwner = this

    lifecycleScope.launch {
      binding.list.adapter = ArrayAdapter(this@ListActivity, android.R.layout.simple_list_item_1, repos("Kotlin"))
    }
  }

  private suspend fun repos(name: String): Array<String> = service
      .repos("$name+in:name,description", 0, 50)
      .items
      .map { it.name }
      .toTypedArray()
}
