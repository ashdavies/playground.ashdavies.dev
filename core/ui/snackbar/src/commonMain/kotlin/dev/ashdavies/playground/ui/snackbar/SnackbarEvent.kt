package dev.ashdavies.playground.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals

public typealias SnackbarEvent = suspend (suspend (SnackbarVisuals) -> SnackbarResult) -> Unit

public suspend operator fun (suspend (SnackbarVisuals) -> SnackbarResult).invoke(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = when (actionLabel) {
        null -> SnackbarDuration.Short
        else -> SnackbarDuration.Indefinite
    },
): SnackbarResult = invoke(
    object : SnackbarVisuals {
        override val message = message
        override val actionLabel = actionLabel
        override val withDismissAction = withDismissAction
        override val duration = duration
    },
)
