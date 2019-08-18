package io.ashdavies.databinding.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@set:BindingAdapter("visible")
var View.visible
  get() = visibility == View.VISIBLE
  set(visible) {
    visibility = if (visible) View.VISIBLE else View.GONE
  }

@set:BindingAdapter("invisible")
var View.invisible
  get() = visibility == View.INVISIBLE
  set(invisible) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
  }

@set:BindingAdapter("gone")
var View.gone
  get() = visibility == View.GONE
  set(gone) {
    visibility = if (gone) View.GONE else View.VISIBLE
  }
