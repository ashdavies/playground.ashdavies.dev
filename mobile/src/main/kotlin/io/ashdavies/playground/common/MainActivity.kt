package io.ashdavies.playground.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.ashdavies.lifecycle.EventObserver
import io.ashdavies.playground.R
import io.ashdavies.playground.binding
import io.ashdavies.playground.databinding.MainActivityBinding
import io.ashdavies.playground.snack
import kotlin.LazyThreadSafetyMode.NONE

internal class MainActivity : AppCompatActivity() {

  private val binding: MainActivityBinding by binding(R.layout.main_activity)
  private val model: MainViewModel by viewModels()

  private val controller: NavController by lazy(NONE) { findNavController(R.id.host) }
  private val coordinator: CoordinatorLayout by lazy(NONE) { binding.coordinator }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding.lifecycleOwner = this

    binding
        .toolbar
        .setupWithNavController(controller)

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
