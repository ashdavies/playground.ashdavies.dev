package io.ashdavies.playground.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import io.ashdavies.playground.compose.isLoading
import io.ashdavies.playground.compose.produceState
import io.ashdavies.playground.compose.rememberEmptyPainter
import io.ashdavies.playground.graph
import io.ashdavies.playground.kotlin.fold
import io.ashdavies.playground.kotlin.isLoading

@Preview
@Composable
internal fun ProfileScreen() = graph(Unit) {
    Column {
        Text("Hello World")

        /*val profile by produceState { profileService.getProfile() }

        Text("Hello Profile")

        val painter: LoadPainter<Any> = profile.fold(
            onSuccess = { rememberCoilPainter(it.picture) },
            onLoading = { rememberEmptyPainter() },
            onFailure = { rememberEmptyPainter() },
        )


        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.placeholder(
                highlight = PlaceholderHighlight.shimmer(),
                visible = profile.isLoading || painter.isLoading,
            )
        )

        Text(
            text = profile.fold({ it.name }, { "Max Mustermann" }),
            modifier = Modifier.placeholder(
                highlight = PlaceholderHighlight.shimmer(),
                visible = profile.isLoading,
            )
        )*/
    }
}
