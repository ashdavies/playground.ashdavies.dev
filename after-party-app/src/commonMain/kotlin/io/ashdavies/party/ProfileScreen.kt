package io.ashdavies.party

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil3.compose.rememberAsyncImagePainter
import io.ashdavies.identity.IdentityState

@Composable
internal fun ProfileActionButton(
    identityState: IdentityState,
    modifier: Modifier = Modifier,
    tintColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit,
) {
    Crossfade(identityState, modifier) { state ->
        when (state) {
            is IdentityState.Authenticated -> IconButton(onClick = onClick) {
                Image(
                    painter = rememberAsyncImagePainter(state.pictureProfileUrl),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape),
                )
            }

            is IdentityState.Failure -> Image(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Failure",
                modifier = Modifier.clickable(onClick = onClick),
                colorFilter = ColorFilter.tint(tintColor),
            )

            IdentityState.Unauthenticated -> IconButton(onClick = onClick) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "SignIn",
                    colorFilter = ColorFilter.tint(tintColor),
                )
            }

            IdentityState.Unsupported -> Unit
        }
    }
}
