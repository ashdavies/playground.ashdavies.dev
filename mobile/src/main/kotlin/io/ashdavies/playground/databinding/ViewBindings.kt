package io.ashdavies.playground.databinding

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setIsVisible(view: View, value: Boolean) {
  view.isVisible = value
}

@BindingAdapter("isInvisible")
fun setIsInvisible(view: View, value: Boolean) {
  view.isInvisible = value
}

@BindingAdapter("isGone")
fun setIsGone(view: View, value: Boolean) {
  view.isGone = value
}
