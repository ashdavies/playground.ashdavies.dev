package dev.ashdavies.playground.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ashdavies.playground.ui.resources.Res
import dev.ashdavies.playground.ui.resources.common_back
import org.jetbrains.compose.resources.stringResource

@Composable
public fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(Res.string.common_back),
        )
    }
}
