package io.ashdavies.playground.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import io.ashdavies.playground.R
import io.ashdavies.playground.databinding.MainActivityBinding
import io.ashdavies.playground.snack
import io.ashdavies.playground.viewBinding
import kotlin.LazyThreadSafetyMode.NONE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val viewBinding: MainActivityBinding by viewBinding(MainActivityBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    private val controller: NavController by lazy(NONE) { findNavController(R.id.host) }
    private val coordinator: CoordinatorLayout by lazy(NONE) { viewBinding.coordinator }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding
            .toolbar
            .setupWithNavController(controller)

        viewModel
            .navErrors
            .onEach { onError(it) }
            .launchIn(lifecycleScope)
    }

    private fun onError(throwable: Throwable) {
        val message: String? = throwable.message
        if (message != null) coordinator.snack(message)
        else coordinator.snack(R.string.unexpected_error)
    }
}
