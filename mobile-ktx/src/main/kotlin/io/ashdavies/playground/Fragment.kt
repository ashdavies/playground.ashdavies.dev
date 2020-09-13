package io.ashdavies.playground

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlin.LazyThreadSafetyMode.NONE

fun Fragment.navController(): Lazy<NavController> = lazy(NONE) {
    findNavController()
}

fun Fragment.setContent(
    content: @Composable () -> Unit
): View = ComposeView(requireContext()).apply {
    setContent(content)
}
