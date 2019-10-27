package io.ashdavies.playground.repos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.ashdavies.lifecycle.EventObserver
import io.ashdavies.playground.R
import io.ashdavies.playground.binding
import io.ashdavies.playground.databinding.MainActivityBinding
import io.ashdavies.playground.setOnQueryTextChanged
import io.ashdavies.playground.snack
import kotlin.LazyThreadSafetyMode.NONE

internal class MainActivity : AppCompatActivity() {

  private val binding: MainActivityBinding by binding(R.layout.main_activity)
  private val coordinator: CoordinatorLayout by lazy(NONE) { binding.coordinator }
  private val controller: NavController by lazy(NONE) { findNavController(this, R.id.host) }
  private val model: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.lifecycleOwner = this

    binding
        .toolbar
        .setupWithNavController(controller)

    binding
        .search
        .setOnQueryTextChanged(model::onQuery)

    model
        .errors
        .observe(this, EventObserver(::error))
  }

  private fun error(throwable: Throwable) {
    val message: String? = throwable.message
    if (message != null) coordinator.snack(message)
    else coordinator.snack(R.string.unexpected_error)
  }
}
