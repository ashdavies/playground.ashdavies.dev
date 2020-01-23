package io.ashdavies.playground

import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View

val View.inflater: LayoutInflater
  get() = from(context)
