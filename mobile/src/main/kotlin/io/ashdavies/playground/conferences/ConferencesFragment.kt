package io.ashdavies.playground.conferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.ashdavies.playground.common.MainViewModel
import io.ashdavies.playground.conferences.ConferencesViewModel.Factory
import io.ashdavies.playground.databinding.ConferencesFragmentBinding
import io.ashdavies.playground.databinding.ConferencesFragmentBinding.inflate
import io.ashdavies.playground.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.LazyThreadSafetyMode.NONE

@ExperimentalCoroutinesApi
internal class ConferencesFragment : Fragment() {

    private val viewBinding: ConferencesFragmentBinding by viewBinding(::inflate)
    private val viewModel: ConferencesViewModel by viewModels { Factory(requireContext()) }
    private val parentModel: MainViewModel by viewModels()

    private val navController: NavController by lazy(NONE) { findNavController() }
    private val adapter = ConferencesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding
            .recycler
            .adapter = adapter

        viewModel
            .navDirections
            .onEach { navController.navigate(it) }
            .launchIn(lifecycleScope)

        viewModel
            .items
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)

        viewModel
            .errors
            .onEach { parentModel.onError(it) }
            .launchIn(lifecycleScope)

        return viewBinding.root
    }
}
