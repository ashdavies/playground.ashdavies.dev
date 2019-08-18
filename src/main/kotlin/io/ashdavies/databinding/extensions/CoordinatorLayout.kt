package io.ashdavies.databinding.extensions

import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

internal fun CoordinatorLayout.snack(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT, block: Snackbar.() -> Unit = { }) = Snackbar
    .make(this, resId, duration)
    .apply(block)
    .show()

internal fun CoordinatorLayout.snack(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, block: Snackbar.() -> Unit = { }) = Snackbar
    .make(this, text, duration)
    .apply(block)
    .show()
