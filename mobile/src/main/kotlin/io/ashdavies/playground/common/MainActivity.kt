package io.ashdavies.playground.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import io.ashdavies.playground.R
import io.ashdavies.playground.databinding.MainActivityBinding
import io.ashdavies.playground.databinding.MainActivityBinding.inflate
import io.ashdavies.playground.navController
import io.ashdavies.playground.snack
import io.ashdavies.playground.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MainActivity : AppCompatActivity() {

    private val navController: NavController by navController(R.id.host)
    private val viewBinding: MainActivityBinding by viewBinding(::inflate)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding
            .toolbar
            .setupWithNavController(navController)

        viewModel
            .navErrors
            .onEach { onError(it) }
            .launchIn(lifecycleScope)
    }

    private fun onError(throwable: Throwable) {
        val coordinator: CoordinatorLayout = viewBinding.coordinator
        when (val message: String? = throwable.message) {
            null -> coordinator.snack(R.string.unexpected_error)
            else -> coordinator.snack(message)
        }
    }
}
