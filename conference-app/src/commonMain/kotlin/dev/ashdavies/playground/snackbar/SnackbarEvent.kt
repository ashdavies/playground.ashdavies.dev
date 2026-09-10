package dev.ashdavies.playground.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals

internal typealias SnackbarEvent = suspend (suspend (SnackbarVisuals) -> SnackbarResult) -> Unit

internal suspend operator fun (suspend (SnackbarVisuals) -> SnackbarResult).invoke(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = when (actionLabel) {
        null -> SnackbarDuration.Short
        else -> SnackbarDuration.Indefinite
    },
) = invoke(
    object : SnackbarVisuals {
        override val message = message
        override val actionLabel = actionLabel
        override val withDismissAction = withDismissAction
        override val duration = duration
    },
)
