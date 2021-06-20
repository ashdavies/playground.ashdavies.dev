package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import io.ashdavies.playground.compose.isLoading
import io.ashdavies.playground.compose.produceState
import io.ashdavies.playground.compose.rememberEmptyPainter
import io.ashdavies.playground.graph

@Composable
internal fun ProfileScreen() = graph(Unit) {
    val profile by produceState { profileService.getProfile() }

    val painter: LoadPainter<Any> = profile.fold(
        onSuccess = { rememberCoilPainter(it.picture) },
        onFailure = { rememberEmptyPainter() },
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.placeholder(
            highlight = PlaceholderHighlight.shimmer(),
            visible = painter.isLoading
        )
    )
}

