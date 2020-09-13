package io.ashdavies.playground.conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import io.ashdavies.playground.conferences.ConferencesViewModel.Factory
import io.ashdavies.playground.navController
import io.ashdavies.playground.setContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
internal class ConferencesFragment : Fragment() {

    private val navController: NavController by navController()
    private val viewModel: ConferencesViewModel by viewModels { Factory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView: View = setContent {
            ConferencesScreen(data = viewModel.items)
        }

        viewModel
            .navDirections
            .onEach { navController.navigate(it) }
            .launchIn(lifecycleScope)

        return contentView
    }
}
