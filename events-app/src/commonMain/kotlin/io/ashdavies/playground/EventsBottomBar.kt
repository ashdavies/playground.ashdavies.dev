package io.ashdavies.playground

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.Screen
import io.ashdavies.playground.home.HomeScreen
import io.ashdavies.playground.profile.ProfileScreen

@Composable
internal fun EventsBottomBar(
    selected: Screen,
    modifier: Modifier = Modifier,
    onClick: (Screen) -> Unit = { },
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        modifier = modifier,
    ) {
        NavigationBarItem(
            icon = { NavigationBarImage(Icons.Default.Home) },
            onClick = { onClick(HomeScreen) },
            selected = selected is HomeScreen,
        )

        NavigationBarItem(
            icon = { NavigationBarImage(Icons.Default.Person) },
            onClick = { onClick(ProfileScreen) },
            selected = selected is ProfileScreen,
        )
    }
}

@Composable
private fun NavigationBarImage(imageVector: ImageVector) {
    Image(
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = null,
        imageVector = imageVector,
    )
}
