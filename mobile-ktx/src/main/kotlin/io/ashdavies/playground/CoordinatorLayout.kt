package io.ashdavies.playground

import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT

fun CoordinatorLayout.snack(@StringRes resId: Int, duration: Int = LENGTH_SHORT, block: Snackbar.() -> Unit = { }) = Snackbar
    .make(this, resId, duration)
    .apply(block)
    .show()

fun CoordinatorLayout.snack(text: CharSequence, duration: Int = LENGTH_SHORT, block: Snackbar.() -> Unit = { }) = Snackbar
    .make(this, text, duration)
    .apply(block)
    .show()
