package io.ashdavies.playground.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.util.Calendar.SHORT
import java.util.Date

private val FORMAT: DateFormat
  get() = getDateInstance(SHORT)

@BindingAdapter("android:text")
fun setText(view: TextView, value: Date) {
  view.text = FORMAT.format(value)
}
